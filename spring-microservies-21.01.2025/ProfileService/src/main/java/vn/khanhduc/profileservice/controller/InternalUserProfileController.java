package vn.khanhduc.profileservice.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.ProfileResponse;
import vn.khanhduc.profileservice.service.UserProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalUserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/users")
    ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(request));
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ProfileResponse> getProfile(@PathVariable
                                               @NotBlank(message = "Id cannot be blank") String id) {
        var data = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/get-user-id/{userId}")
    ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable Long userId) {
        var data = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(data);
    }

}
