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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/profiles")
    ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(request));
    }

    @GetMapping("/profiles/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ProfileResponse> getProfiles(@PathVariable
                                                @NotBlank(message = "Id cannot be blank") String id) {
        var data = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(data);
    }

}
