package vn.khanhduc.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.ResponseData;
import vn.khanhduc.identityservice.dto.response.SignInResponse;
import vn.khanhduc.identityservice.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ResponseData<SignInResponse> signIn(@RequestBody @Valid SignInRequest request) {
        var result = authenticationService.signIn(request);

        return ResponseData.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Sign in success")
                .data(result)
                .build();
    }

}
