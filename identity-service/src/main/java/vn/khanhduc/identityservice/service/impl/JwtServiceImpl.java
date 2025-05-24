package vn.khanhduc.identityservice.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.common.TokenType;
import vn.khanhduc.identityservice.exception.AuthenticationException;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.entity.User;
import vn.khanhduc.identityservice.service.JwtService;
import vn.khanhduc.identityservice.service.RedisService;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import static vn.khanhduc.identityservice.constant.ClaimsName.*;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RedisService redisService;

    @Override
    public String generateToken(User user,  TokenType tokenType) {
        JWSAlgorithm algorithm = tokenType == TokenType.ACCESS_TOKEN ? JWSAlgorithm.HS384 : JWSAlgorithm.HS512;

        JWSHeader header = new JWSHeader(algorithm);

        long expired = tokenType == TokenType.ACCESS_TOKEN ? 30 : 14;
        Date expiredTime = new Date(Instant.now().plus(expired, tokenType == TokenType.ACCESS_TOKEN
                ? ChronoUnit.SECONDS
                : ChronoUnit.DAYS)
                .toEpochMilli());

        JWTClaimsSet claimsSet =  new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(expiredTime)
                .jwtID(UUID.randomUUID().toString())
                .claim(AUTHORITIES, buildAuthority(user))
                .claim(TOKEN_TYPE, tokenType.name())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public SignedJWT verificationToken(String token, boolean isRefreshToken) throws ParseException, JOSEException {
        if (token == null || token.trim().isEmpty())
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);

        SignedJWT signedJWT = SignedJWT.parse(token); // nếu token bị chỉnh sửa nó sẽ throw exception tại đây và không chạy xuống dưới

        String tokenType = isRefreshToken ? TokenType.REFRESH_TOKEN.name() : TokenType.ACCESS_TOKEN.name();

        if(StringUtils.isBlank(tokenType) || !signedJWT.getJWTClaimsSet().getClaim(TOKEN_TYPE).equals(tokenType)) {
            throw new AuthenticationException(ErrorCode.TOKEN_INVALID);
        }

        var expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        if(expiration.before(new Date()))
            throw new AuthenticationException(ErrorCode.UNAUTHENTICATED);

        if(redisService.getToken(signedJWT.getJWTClaimsSet().getJWTID()) != null) {
            throw new AuthenticationException(ErrorCode.TOKEN_BLACK_LIST);
        }

        var verified = signedJWT.verify(new MACVerifier(secretKey));
        if(!verified)
            throw new AuthenticationException(ErrorCode.TOKEN_INVALID);
        return signedJWT;
    }

    @Override
    public String buildAuthority(User user) {
        StringJoiner joiner = new StringJoiner(", ");

        user.getUserHasRoles().stream()
                .map(r -> r.getRole().getName())
                .forEach(joiner::add);
        return joiner.toString();
    }

    @Override
    public long extractTokenExpired(String token) {
        try {
            long expirationTime = SignedJWT.parse(token).getJWTClaimsSet()
                    .getExpirationTime().getTime();
            long now = System.currentTimeMillis();
            long ttl = expirationTime - now;
            return ttl <= 0 ? 1000 : ttl;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
