package vn.khanhduc.identityservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
