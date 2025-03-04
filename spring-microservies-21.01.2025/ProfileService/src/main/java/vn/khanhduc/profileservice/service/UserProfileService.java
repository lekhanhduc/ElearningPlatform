package vn.khanhduc.profileservice.service;

import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;

public interface UserProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getUserProfile(String id);
    PageResponse<ProfileResponse> getAllProfile(int page, int size);
    ProfileResponse getProfileByUserId(Long userId);
}
