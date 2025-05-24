package vn.khanhduc.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.identityservice.dto.request.ChangePasswordRequest;
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

    @GetMapping("/fetch-users/{id}")
    public ResponseData<UserDetailResponse> getUserById(@PathVariable Long id) {
        var result = userService.getUserById(id);
        return ResponseData.<UserDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get User by Id")
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

    @GetMapping("/profile/template/{id}")
    ResponseData<UserProfileResponse> getProfileWithRestTemplate(@PathVariable String id) {
        var result = userService.getUserProfileByIdWithRestTemplate(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/profile/webclient/{id}")
    ResponseData<UserProfileResponse> getProfileWithWebClient(@PathVariable String id) {
        var result = userService.getUserProfileByIdWithWebClient(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/profile/openfeign/{id}")
    ResponseData<UserProfileResponse> getProfileWithOpenFeign(@PathVariable Long id) {
        var result = userService.getUserProfileByIdWithOpenFeign(id);
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get Profile User from Profile-Service")
                .data(result)
                .build();
    }

    @GetMapping("/info")
    ResponseData<UserProfileResponse> getInfoUserCurrentLogin() {
        return ResponseData.<UserProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .data(userService.getInfoUserLogin())
                .build();
    }

    @PostMapping("/change-password")
    ResponseData<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseData.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change password success")
                .build();
    }

}
