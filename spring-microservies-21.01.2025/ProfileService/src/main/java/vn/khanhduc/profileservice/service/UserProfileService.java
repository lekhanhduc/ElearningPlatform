package vn.khanhduc.profileservice.service;

import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;

public interface UserProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getUserProfile(String id);
}
