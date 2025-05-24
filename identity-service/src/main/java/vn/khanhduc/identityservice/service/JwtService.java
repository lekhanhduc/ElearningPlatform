package vn.khanhduc.identityservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import vn.khanhduc.identityservice.common.TokenType;
import vn.khanhduc.identityservice.entity.User;
import java.text.ParseException;

public interface JwtService {

    String generateToken(User user, TokenType tokenType);
    SignedJWT verificationToken(String token, boolean isRefreshToken) throws ParseException, JOSEException;
    String buildAuthority(User user);
    long extractTokenExpired(String token);
}
