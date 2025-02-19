package vn.khanhduc.profileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.service.UserProfileService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InternalUserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/internal/profiles")
    ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(request));
    }

}
