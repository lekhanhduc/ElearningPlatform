package com.example.userservice.service;

import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.dto.response.PageResponse;
import com.example.userservice.dto.response.UserDetailResponse;

public interface UserService {
    void saveUser(UserRequest request);
    PageResponse<UserDetailResponse> getUserDetails(int page, int size);
}
