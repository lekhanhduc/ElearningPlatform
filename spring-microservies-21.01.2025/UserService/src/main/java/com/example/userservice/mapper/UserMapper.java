package com.example.userservice.mapper;

import com.example.userservice.dto.response.UserDetailResponse;
import com.example.userservice.model.User;

public class UserMapper {

    private UserMapper() {}

    public static UserDetailResponse toUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .userStatus(user.getUserStatus())
                .build();
    }
}
