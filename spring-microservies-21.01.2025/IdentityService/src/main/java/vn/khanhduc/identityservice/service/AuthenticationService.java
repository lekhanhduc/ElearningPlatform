package vn.khanhduc.identityservice.service;

import com.nimbusds.jose.JOSEException;
import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.RefreshTokenResponse;
import vn.khanhduc.identityservice.dto.response.SignInResponse;
import vn.khanhduc.identityservice.dto.response.TokenVerificationResponse;

import java.text.ParseException;

public interface AuthenticationService {
    SignInResponse signIn(SignInRequest request);
    TokenVerificationResponse introspect(String token);
    RefreshTokenResponse refreshToken(String accessToken, String refreshToken) throws ParseException, JOSEException;
    void signOut(String accessToken);
}
