package vn.khanhduc.paymentservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.khanhduc.paymentservice.configuration.AuthenticationRequestInterceptor;
import vn.khanhduc.paymentservice.dto.response.ResponseData;
import vn.khanhduc.paymentservice.dto.response.UserDetailResponse;

@FeignClient(name = "IDENTITY-SERVICE", configuration = {AuthenticationRequestInterceptor.class})
public interface UserClient {

    @GetMapping(value = "/identity/api/v1/fetch-users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<UserDetailResponse> getUserDetail(@PathVariable("userId") Long userId);

}
