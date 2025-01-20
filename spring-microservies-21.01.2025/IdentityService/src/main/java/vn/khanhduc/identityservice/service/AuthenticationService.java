package vn.khanhduc.identityservice.service;

import vn.khanhduc.identityservice.dto.request.SignInRequest;
import vn.khanhduc.identityservice.dto.response.SignInResponse;

public interface AuthenticationService {
    SignInResponse signIn(SignInRequest request);
}
