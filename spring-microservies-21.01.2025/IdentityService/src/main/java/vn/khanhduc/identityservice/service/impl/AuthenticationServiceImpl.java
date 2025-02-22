package vn.khanhduc.identityservice.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.RefreshTokenResponse;
import vn.khanhduc.identityservice.dto.response.SignInResponse;
import vn.khanhduc.identityservice.dto.response.TokenVerificationResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.service.AuthenticationService;
import vn.khanhduc.identityservice.service.JwtService;
import vn.khanhduc.identityservice.service.RedisService;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;

    @Override
//    @CircuitBreaker(name = "identity-service", fallbackMethod = "signInFallback")
//    @Retry(name = "${spring.application.name}", fallbackMethod = "signInFallback")
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

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        cookie.setMaxAge(14 * 24 * 60 * 60); // 2 tuần


        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public TokenVerificationResponse introspect(String token) {
        boolean isValid = true;
        try {
            jwtService.verificationToken(token);
        } catch (ParseException | JOSEException e) {
            isValid = false;
        }
        return TokenVerificationResponse.builder()
                .isValid(isValid)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(String accessToken, String refreshToken) throws ParseException, JOSEException {
        if(StringUtils.isBlank(refreshToken))
            throw new IdentityException(ErrorCode.REFRESH_TOKEN_INVALID);
        // 1. Kiểm tra tính hợp lệ của token trước rồi mới tiến hành bước tiếp theo
        SignedJWT signedJWT = jwtService.verificationToken(refreshToken);

        // 2. Kiểm tra thêm -> tăng tính bảo mật
        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        if(!user.getRefreshToken().equals(refreshToken))
            throw new IdentityException(ErrorCode.REFRESH_TOKEN_INVALID);

        // 3. Đưa accessToken cũ vào black list
        if(StringUtils.isNotBlank(accessToken)) {
            SignedJWT jwt = SignedJWT.parse(accessToken);
            var jwtTid = jwt.getJWTClaimsSet().getJWTID();
            long accessTokenExp = jwtService.extractTokenExpired(accessToken);
            log.info("jid {}", jwtTid);
            redisService.save(jwtTid, accessToken, accessTokenExp, TimeUnit.MILLISECONDS);
        }
        // 4. gửi về client accessToken mới
        String newAccessToken = jwtService.generateAccessToken(user);
        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Override
    public void signOut(String accessToken) {
        if(StringUtils.isBlank(accessToken))
            throw new IdentityException(ErrorCode.ACCESS_TOKEN_EMPTY);
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            String jid = signedJWT.getJWTClaimsSet().getJWTID();
            long tokenExpired = jwtService.extractTokenExpired(accessToken);
            redisService.save(jid, accessToken, tokenExpired, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new IdentityException(ErrorCode.SIGN_OUT_FAILED);
        }
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
