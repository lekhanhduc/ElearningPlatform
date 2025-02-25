package vn.khanhduc.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.ResponseData;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;
import vn.khanhduc.identityservice.service.UserService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/registration")
    ResponseData<UserCreationResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createUser(request);

        return ResponseData.<UserCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("User created")
                .data(result)
                .build();
    }

    @GetMapping("/users")
    ResponseData<List<UserDetailResponse>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "page", required = false, defaultValue = "1") int size
    ) {
        var result = userService.getAllUser(page, size);
        return ResponseData.<List<UserDetailResponse>>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get All User")
                .data(result)
                .build();
    }

    @GetMapping("/users/{id}")
    ResponseData<UserProfileResponse> getProfileWithRestClient(@PathVariable String id) {
        var result = userService.getUserProfileByIdWithRestClient(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/users/template/{id}")
    ResponseData<UserProfileResponse> getProfileWithRestTemplate(@PathVariable String id) {
        var result = userService.getUserProfileByIdWithRestTemplate(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/users/webclient/{id}")
    ResponseData<UserProfileResponse> getProfileWithWebClient(@PathVariable String id) {
        var result = userService.getUserProfileByIdWithWebClient(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/users/openfeign/{id}")
    ResponseData<UserProfileResponse> getProfileWithOpenFeign(@PathVariable Long id) {
        var result = userService.getUserProfileByIdWithOpenFeign(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

}
