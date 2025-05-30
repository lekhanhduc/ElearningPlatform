package vn.khanhduc.identityservice.service;

import vn.khanhduc.identityservice.dto.request.ChangePasswordRequest;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;
import java.util.List;

public interface UserService {

    UserCreationResponse createUser(UserCreationRequest request);
    List<UserDetailResponse> getAllUser(int page, int size);
    UserDetailResponse getUserById(Long id);
    UserProfileResponse getUserProfileByIdWithRestTemplate(String id);
    UserProfileResponse getUserProfileByIdWithWebClient(String id);
    UserProfileResponse getUserProfileByIdWithOpenFeign(Long id);
    UserProfileResponse getInfoUserLogin();
    void changePassword(ChangePasswordRequest request);
}
