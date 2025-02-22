package vn.khanhduc.profileservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.exception.ResourceNotFoundException;
import vn.khanhduc.profileservice.mapper.UserProfileMapper;
import vn.khanhduc.profileservice.model.UserProfile;
import vn.khanhduc.profileservice.repository.UserProfileRepository;
import vn.khanhduc.profileservice.service.UserProfileService;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-PROFILE-SERVICE")
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfileRepository.save(userProfile);
        log.info("Created profile successfully");
        return userProfileMapper.toProfileResponse(userProfile);
    }

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
                .stream().map(profile -> ProfileResponse.builder().build())
                .toList();
        return PageResponse.<ProfileResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(userProfiles.getTotalPages())
                .totalElements(userProfiles.getTotalElements())
                .data(responses)
                .build();
    }

}
