package com.example.userservice.service.impl;

import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.dto.response.PageResponse;
import com.example.userservice.dto.response.UserDetailResponse;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void saveUser(UserRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserStatus(request.getUserStatus());
        if(StringUtils.isNotBlank(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if(StringUtils.isNotBlank(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if(StringUtils.isNotBlank(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if(StringUtils.isNotBlank(request.getUserStatus())) {
            user.setFirstName(request.getFirstName());
        }
        if(StringUtils.isNotBlank(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if(StringUtils.isNotBlank(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        log.info("Saving user success ");
        userRepository.save(user);
    }

    @Override
    public PageResponse<UserDetailResponse> getUserDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page -1 , size);
        Page<User> users = userRepository.findAll(pageable);

        return PageResponse.<UserDetailResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .data(users.getContent().stream()
                        .map(UserMapper::toUserDetailResponse)
                        .toList())
                .build();
    }
}
