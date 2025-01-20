package vn.khanhduc.identityservice.service;

import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;

import java.util.List;

public interface UserService {

    UserCreationResponse createUser(UserCreationRequest request);

    List<UserDetailResponse> getAllUser();
}
