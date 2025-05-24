package vn.khanhduc.profileservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.event.dto.ProfileCreationFailedEvent;
import vn.khanhduc.event.dto.ProfileEvent;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.request.ProfileUpdateRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
import vn.khanhduc.profileservice.dto.response.ProfileDetailResponse;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.exception.AuthenticationException;
import vn.khanhduc.profileservice.exception.ResourceNotFoundException;
import vn.khanhduc.profileservice.mapper.UserProfileMapper;
import vn.khanhduc.profileservice.entity.UserProfile;
import vn.khanhduc.profileservice.repository.UserProfileRepository;
import vn.khanhduc.profileservice.repository.http.FileClient;
import vn.khanhduc.profileservice.service.UserProfileService;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-PROFILE-SERVICE")
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final S3Service s3Service;
    private final FileClient fileClient;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfileRepository.save(userProfile);
        log.info("Created profile successfully");
        return userProfileMapper.toProfileResponse(userProfile);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public ProfileResponse getUserProfile(String id) {
        return userProfileRepository.findById(id)
                .map(userProfileMapper::toProfileResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Profile Not Found"));
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    @Override
    public PageResponse<ProfileResponse> getAllProfile(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        Page<UserProfile> userProfiles = userProfileRepository.findAll(pageable);
        List<ProfileResponse> responses = userProfiles.getContent()
                .stream().map(profile -> ProfileResponse.builder()

                        .build())
                .toList();
        return PageResponse.<ProfileResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(userProfiles.getTotalPages())
                .totalElements(userProfiles.getTotalElements())
                .data(responses)
                .build();
    }

    @Override
    public ProfileResponse getProfileByUserId(Long userId) {
        log.info("User id: {}", userId);
        return userProfileRepository.findByUserId(userId)
                .map(userProfileMapper::toProfileResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Profile Not Found"));
    }

    @KafkaListener(topics = "user-created", groupId = "profile-group")
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            label = "createProfileRetry")
    @Override
    public void createProfile(ProfileEvent profileEvent, Acknowledgment acknowledgment) {
       try {
           log.info("Create Profile Start ");
           userProfileRepository.save(
                   UserProfile.builder()
                           .userId(profileEvent.getUserId())
                           .firstName(profileEvent.getFirstName())
                           .lastName(profileEvent.getLastName())
                           .phoneNumber(profileEvent.getPhoneNumber())
                           .avatar(profileEvent.getAvatar())
                           .build());
           acknowledgment.acknowledge();
           log.info("Create profile successfully");
           kafkaTemplate.send("profile-created", profileEvent);
       }catch (Exception e) {
           log.info("Create profile failed");
           ProfileCreationFailedEvent failedEvent = ProfileCreationFailedEvent.builder()
                   .userId(profileEvent.getUserId())
                   .build();
           kafkaTemplate.send("profile-create-failed", failedEvent);
           throw e;
       }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ProfileDetailResponse updateProfile(ProfileUpdateRequest request, MultipartFile file) {
        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty())
            throw new AuthenticationException("Unauthenticated");

        Long userId = Long.parseLong(principal.get());
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        userProfileMapper.updateProfile(request, userProfile);

        if(file != null) {
            String urlAvatar = s3Service.uploadFileToMinIO("upload", file);
            userProfile.setAvatar(urlAvatar);
            log.info("Update avatar to S3 successfully");
        }
        userProfileRepository.save(userProfile);
        log.info("Update profile successfully");
        return ProfileDetailResponse.builder()
                .profileId(userProfile.getId())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .phoneNumber(userProfile.getPhoneNumber())
                .avatar(userProfile.getAvatar())
                .linkYoutube(userProfile.getLinkYoutube())
                .linkFacebook(userProfile.getLinkFacebook())
                .linkLinkedIn(userProfile.getLinkLinkedIn())
                .build();
    }

    @Override
    public ProfileResponse uploadAvatar(MultipartFile file) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty())
            throw new AuthenticationException("Unauthenticated");

        Long userId = Long.parseLong(principal.get());
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if(file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        var response = fileClient.uploadFile(file);
        profile.setAvatar(response.getData().getUrl());

        userProfileRepository.save(profile);

        return ProfileResponse.builder()
                .userId(userId)
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .avatar(profile.getAvatar())
                .phoneNumber(profile.getPhoneNumber())
                .build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ProfileDetailResponse getProfileByUserLogin() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty())
            throw new AuthenticationException("Unauthenticated");
        Long userId = Long.parseLong(principal.get());

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not existed"));

        return ProfileDetailResponse.builder()
                .profileId(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .avatar(profile.getAvatar())
                .linkFacebook(profile.getLinkFacebook())
                .linkLinkedIn(profile.getLinkLinkedIn())
                .linkYoutube(profile.getLinkYoutube())
                .build();
    }

}
