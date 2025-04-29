package vn.khanhduc.profileservice.service;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.event.dto.ProfileEvent;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.request.ProfileUpdateRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;

public interface UserProfileService {
    ProfileResponse createProfile(ProfileRequest request);
    ProfileResponse getUserProfile(String id);
    PageResponse<ProfileResponse> getAllProfile(int page, int size);
    ProfileResponse getProfileByUserId(Long userId);
    void createProfile(ProfileEvent event, Acknowledgment acknowledgment);
    ProfileResponse updateProfile(ProfileUpdateRequest request, MultipartFile file);
    ProfileResponse uploadAvatar(MultipartFile file);
}
