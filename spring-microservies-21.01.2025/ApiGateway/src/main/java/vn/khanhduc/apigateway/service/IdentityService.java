package vn.khanhduc.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.khanhduc.apigateway.dto.response.ResponseData;
import vn.khanhduc.apigateway.dto.response.TokenVerificationResponse;
import vn.khanhduc.apigateway.repository.IdentityClient;

@Service
@RequiredArgsConstructor
public class IdentityService {

    private final IdentityClient identityClient;

    public Mono<ResponseData<TokenVerificationResponse>> verificationToken(String token) {
        return identityClient.verificationToken(token);
    }
}
