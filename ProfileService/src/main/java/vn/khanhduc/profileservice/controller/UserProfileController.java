package vn.khanhduc.profileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.request.ProfileUpdateRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.exception.AuthenticationException;
import vn.khanhduc.profileservice.exception.ResourceNotFoundException;
import vn.khanhduc.profileservice.service.UserProfileService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/profiles")
    ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(request));
    }

    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<PageResponse<ProfileResponse>> getAllProfile(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "page", required = false, defaultValue = "1") int size
    ) {
        var data = userProfileService.getAllProfile(page, size);
        return ResponseEntity.ok(data);
    }

    @PutMapping("/update")
    ResponseEntity<ProfileResponse> updateProfile(@RequestPart(required = false, value = "request") ProfileUpdateRequest request,
                                                  @RequestPart(required = false, value = "avatar") MultipartFile avatar) {
        try {
            var result = userProfileService.updateProfile(request, avatar);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/upload-avatar")
    ResponseEntity<ProfileResponse> uploadAvatar(@RequestPart(required = false, value = "avatar") MultipartFile avatar) {
        return ResponseEntity.ok(userProfileService.uploadAvatar(avatar));
    }


}
