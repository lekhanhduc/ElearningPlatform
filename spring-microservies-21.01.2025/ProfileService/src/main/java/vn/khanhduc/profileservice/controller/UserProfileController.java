package vn.khanhduc.profileservice.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.profileservice.dto.request.ProfileRequest;
import vn.khanhduc.profileservice.dto.response.PageResponse;
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
    ResponseEntity<ProfileResponse> getProfile(@PathVariable
                                                @NotBlank(message = "Id cannot be blank") String id) {
        var data = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(data);
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

}
