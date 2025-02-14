package vn.khanhduc.profileservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.exception.ResourceNotFoundException;
import vn.khanhduc.profileservice.mapper.UserProfileMapper;
import vn.khanhduc.profileservice.model.UserProfile;
import vn.khanhduc.profileservice.repository.UserProfileRepository;
import vn.khanhduc.profileservice.service.UserProfileService;

import java.lang.module.ResolutionException;

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
        return userProfileMapper.toProfileResponse(userProfile);
    }

    @Override
    public ProfileResponse getUserProfile(String id) {
        return userProfileRepository.findById(id)
                .map(userProfileMapper::toProfileResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Profile Not Found"));
    }
}
