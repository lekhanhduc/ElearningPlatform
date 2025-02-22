package com.example.userservice.controller;

import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.dto.response.PageResponse;
import com.example.userservice.dto.response.UserDetailResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/fetch-all")
    public ResponseEntity<PageResponse<UserDetailResponse>> fetchAllUser(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getUserDetails(page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> saveUser(@RequestBody UserRequest request) {
        userService.saveUser(request);
        return ResponseEntity.noContent().build();
    }

}
