package vn.khanhduc.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.khanhduc.identityservice.dto.request.SaveUserRequest;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @PostMapping("users/api/v1/create")
    void SaveUser(@RequestBody SaveUserRequest request);
}
