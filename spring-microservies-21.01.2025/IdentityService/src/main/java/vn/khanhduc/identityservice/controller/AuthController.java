package vn.khanhduc.identityservice.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.RefreshTokenResponse;
import vn.khanhduc.identityservice.dto.response.ResponseData;
import vn.khanhduc.identityservice.dto.response.SignInResponse;
import vn.khanhduc.identityservice.dto.response.TokenVerificationResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.service.AuthenticationService;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j(topic = "AUTH-CONTROLLER")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ResponseData<SignInResponse> signIn(@RequestBody @Valid SignInRequest request, HttpServletResponse response) {
        var result = authenticationService.signIn(request);

        Cookie cookie = new Cookie("refresh_token", result.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        cookie.setMaxAge(14 * 24 * 60 * 60);
        response.addCookie(cookie);

        return ResponseData.<SignInResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Sign in success")
                .data(result)
                .build();
    }

    @PostMapping("/verification-token")
    ResponseData<TokenVerificationResponse> verificationToken(@RequestHeader(name = "Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        var result = authenticationService.introspect(token);
        return ResponseData.<TokenVerificationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Verification token")
                .data(result)
                .build();
    }

    @PostMapping("/refresh-token")
    ResponseData<RefreshTokenResponse> refreshToken(
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @CookieValue(name = "refresh_token") String refreshToken) {
        try {
            String accessToken = authHeader.replace("Bearer ", "");
            RefreshTokenResponse result = authenticationService.refreshToken(accessToken, refreshToken);
            return ResponseData.<RefreshTokenResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Refresh token")
                    .data(result)
                    .build();
        } catch (ParseException | JOSEException e) {
            log.error("Refresh token failed");
            throw new IdentityException(ErrorCode.REFRESH_TOKEN_INVALID);
        }
    }

    @PostMapping("/logout")
    ResponseData<Void> signOut(@RequestHeader(name = "Authorization") String authHeader, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setDomain("localhost");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        String token = authHeader.replace("Bearer ", "");
        authenticationService.signOut(token);

        return ResponseData.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Sign out success")
                .build();
    }

}
