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

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet =  new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("identity-service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(60, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("Authority", buildAuthority(user))
                .claim("Permission", buildPermissions(user))
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
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

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
    public String ExtractUserName(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new IdentityException(ErrorCode.TOKEN_INVALID);
        }
    }

    @Override
    public boolean verificationToken(String token, User user) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        var email = signedJWT.getJWTClaimsSet().getSubject();
        var expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        if( !Objects.equals(email, user.getEmail())) {
            log.error("Email in token not match email system");
            throw new IdentityException(ErrorCode.TOKEN_INVALID);
        }
        if(expiration.before(new Date())) {
            log.error("Token expired");
            throw new IdentityException(ErrorCode.TOKEN_INVALID);
        }
        return signedJWT.verify(new MACVerifier(secretKey));
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

}
