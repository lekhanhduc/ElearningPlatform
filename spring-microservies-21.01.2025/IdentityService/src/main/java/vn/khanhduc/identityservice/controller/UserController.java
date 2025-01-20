package vn.khanhduc.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.ResponseData;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.service.UserService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/users-creation")
    ResponseData<UserCreationResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createUser(request);

        return ResponseData.<UserCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("User created")
                .data(result)
                .build();
    }

    @GetMapping("/users")
    ResponseData<List<UserDetailResponse>> getAll() {
        var result = userService.getAllUser();
        return ResponseData.<List<UserDetailResponse>>builder()
                .code(HttpStatus.CREATED.value())
                .message("Get All User")
                .data(result)
                .build();
    }
}
