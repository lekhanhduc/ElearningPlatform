package vn.khanhduc.identityservice.repository.httpclient;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import vn.khanhduc.identityservice.dto.request.ExchangeTokenRequest;
import vn.khanhduc.identityservice.dto.response.ExchangeTokenResponse;

@FeignClient(value = "identity", url = "https://oauth2.googleapis.com")
public interface GoogleClient {

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
