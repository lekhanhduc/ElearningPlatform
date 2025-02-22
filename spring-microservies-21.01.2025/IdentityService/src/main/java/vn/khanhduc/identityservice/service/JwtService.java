package vn.khanhduc.identityservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import vn.khanhduc.identityservice.model.User;
import java.text.ParseException;

public interface JwtService {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    SignedJWT verificationToken(String token) throws ParseException, JOSEException;
    String buildAuthority(User user);
    String buildPermissions(User user);
    long extractTokenExpired(String token);
}
