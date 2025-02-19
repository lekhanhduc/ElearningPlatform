package vn.khanhduc.identityservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.SignInResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.service.AuthenticationService;
import vn.khanhduc.identityservice.service.JwtService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @CircuitBreaker(name = "identity-service", fallbackMethod = "signInFallback")
    public SignInResponse signIn(SignInRequest request) {
        log.info("Authentication start ...!");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public SignInResponse signInFallback(SignInRequest request, Throwable throwable) {
        log.error("Lỗi khi gọi service đăng nhập: {}", throwable.getMessage());
        String errorMessage = "Hệ thống xác thực đang gặp sự cố. Vui lòng thử lại sau hoặc liên hệ bộ phận hỗ trợ.";
        return SignInResponse.builder()
                .accessToken(errorMessage)
                .refreshToken(errorMessage)
                .userId(-1L)
                .build();
    }

}
