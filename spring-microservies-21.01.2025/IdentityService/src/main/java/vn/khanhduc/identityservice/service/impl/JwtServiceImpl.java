package vn.khanhduc.identityservice.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.service.JwtService;
import vn.khanhduc.identityservice.service.RedisService;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RedisService redisService;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet =  new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("identity-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(60, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("authority", buildAuthority(user))
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
    public String generateRefreshToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        var claimsSet =  new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("identity-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        var payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public SignedJWT verificationToken(String token) throws ParseException, JOSEException {
        if (token == null || token.trim().isEmpty())
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);

        SignedJWT signedJWT = SignedJWT.parse(token); // nếu token bị chỉnh sửa nó sẽ throw exception tại đây và không chạy xuống dưới

        if(redisService.getToken(signedJWT.getJWTClaimsSet().getJWTID()) != null) {
            throw new IdentityException(ErrorCode.TOKEN_BLACK_LIST);
        }
        var expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        if(expiration.before(new Date()))
            throw new IdentityException(ErrorCode.TOKEN_INVALID);

        var verified = signedJWT.verify(new MACVerifier(secretKey));
        if(!verified)
            throw new IdentityException(ErrorCode.TOKEN_INVALID);
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
    public String buildPermissions(User user) {
        StringJoiner joiner = new StringJoiner(", ");
        user.getUserHasRoles().stream()
                .flatMap(r -> r.getRole().getRoleHasPermissions().stream())
                .map(p -> p.getPermission().getName())
                .forEach(joiner::add);
        return joiner.toString();
    }

    @Override
    public long extractTokenExpired(String token) {
        try {
            long expirationTime = SignedJWT.parse(token).getJWTClaimsSet()
                    .getExpirationTime().getTime();
            long now = System.currentTimeMillis();
            return Math.max(expirationTime - now, 0);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
