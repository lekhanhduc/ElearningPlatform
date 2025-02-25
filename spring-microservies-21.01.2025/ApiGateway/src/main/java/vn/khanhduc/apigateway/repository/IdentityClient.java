package vn.khanhduc.apigateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;
import vn.khanhduc.apigateway.dto.response.ResponseData;
import vn.khanhduc.apigateway.dto.response.TokenVerificationResponse;

public interface IdentityClient {

    @PostExchange(url = "/api/v1/auth/verification-token", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseData<TokenVerificationResponse>> verificationToken(@RequestHeader(name = "Authorization") String authHeader);
}
