package vn.khanhduc.identityservice.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.khanhduc.event.dto.ProfileEvent;
import vn.khanhduc.identityservice.common.TokenType;
import vn.khanhduc.identityservice.common.UserStatus;
import vn.khanhduc.identityservice.common.UserType;
import vn.khanhduc.identityservice.dto.request.ExchangeTokenRequest;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.*;
import vn.khanhduc.identityservice.entity.Role;
import vn.khanhduc.identityservice.entity.UserHasRole;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.entity.User;
import vn.khanhduc.identityservice.repository.RoleRepository;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.repository.httpclient.GoogleClient;
import vn.khanhduc.identityservice.repository.httpclient.OutboundUserClient;
import vn.khanhduc.identityservice.service.AuthenticationService;
import vn.khanhduc.identityservice.service.JwtService;
import vn.khanhduc.identityservice.service.RedisService;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${google.client-id}")
    private String CLIENT_ID;

    @Value("${google.secret-key}")
    private String CLIENT_SECRET;

    @Value("${google.redirect-uri}")
    private String REDIRECT_URI;

    private static final String GRANT_TYPE = "authorization_code";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final GoogleClient googleClient;
    private final RoleRepository roleRepository;
    private final OutboundUserClient outboundUserClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public SignInResponse signIn(SignInRequest request) {
        log.info("Authentication start ...!");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = (User) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
            String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);

            redisService.save(user.getId().toString(), refreshToken, 14, TimeUnit.DAYS);

            return SignInResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userType(user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()))
                    .build();
        } catch (AuthenticationException e) {
            log.error("Authentication exception");
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SignInResponse signInWithGoogle(String code) {
        ExchangeTokenResponse tokenResponse = googleClient.exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                .build());
        String accessTokenGoogle = tokenResponse.getAccessToken();

        OutboundUserResponse userProfile = outboundUserClient.getUserInfo(accessTokenGoogle);

        User user = userRepository.findByEmail(userProfile.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(userProfile.getEmail())
                            .userStatus(UserStatus.ACTIVE)
                            .build();
                    Role role = roleRepository.findByName(String.valueOf(UserType.USER))
                            .orElseGet(() -> roleRepository.save(Role.builder().name(String.valueOf(UserType.USER)).build()));
                    UserHasRole userHasRole = UserHasRole.builder().role(role).user(newUser).build();
                    newUser.setUserHasRoles(new HashSet<>(Set.of(userHasRole)));
                    userRepository.save(newUser);

                    ProfileEvent profile = ProfileEvent.builder()
                            .email(userProfile.getEmail())
                            .userId(newUser.getId())
                            .firstName(userProfile.getGivenName())
                            .lastName(userProfile.getFamilyName())
                            .avatar(userProfile.getPicture())
                            .build();

                    kafkaTemplate.send("user-created", profile);
                    return newUser;
                });

        var accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        var refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        redisService.save(user.getId().toString(), refreshToken, 14, TimeUnit.DAYS);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public TokenVerificationResponse introspect(String token) {
        boolean isValid = true;
        try {
            jwtService.verificationToken(token, false);
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

        SignedJWT signedJWT = jwtService.verificationToken(refreshToken, true);

        String userId = signedJWT.getJWTClaimsSet().getSubject();
        String storedRefreshToken = redisService.getToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new IdentityException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        // 3. Đưa accessToken cũ vào black list
        if(StringUtils.isNotBlank(accessToken)) {
            SignedJWT jwt = SignedJWT.parse(accessToken);
            var jwtTid = jwt.getJWTClaimsSet().getJWTID();
            long accessTokenExp = jwtService.extractTokenExpired(accessToken);
            log.info("jid {}", jwtTid);
            redisService.save(jwtTid, accessToken, accessTokenExp, TimeUnit.MILLISECONDS);
        }
        // 4. gửi về client accessToken mới
        String newAccessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
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
            if(redisService.getToken(signedJWT.getJWTClaimsSet().getJWTID()) != null){
                log.error("Logout error, You are logged out");
                throw new IdentityException(ErrorCode.SIGN_OUT_FAILED);
            }

            String jid = signedJWT.getJWTClaimsSet().getJWTID();
            long tokenExpired = jwtService.extractTokenExpired(accessToken);

            redisService.save(jid, accessToken, tokenExpired, TimeUnit.MILLISECONDS);
            redisService.delete(signedJWT.getJWTClaimsSet().getSubject());
            log.info("Logout successfully");
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new IdentityException(ErrorCode.SIGN_OUT_FAILED);
        }
    }

}
