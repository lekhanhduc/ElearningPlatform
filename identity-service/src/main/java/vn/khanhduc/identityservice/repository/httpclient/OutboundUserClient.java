package vn.khanhduc.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.khanhduc.identityservice.dto.response.OutboundUserResponse;

@FeignClient(value = "outbound-user-client", url = "https://openidconnect.googleapis.com")
public interface OutboundUserClient {

    @GetMapping(value = "/v1/userinfo")
    OutboundUserResponse getUserInfo(@RequestParam("access_token") String accessToken);
}
