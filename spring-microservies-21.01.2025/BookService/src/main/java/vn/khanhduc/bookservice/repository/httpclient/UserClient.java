package vn.khanhduc.bookservice.repository.httpclient;

import vn.khanhduc.bookservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROFILE-SERVICE")
public interface UserClient {

    @GetMapping(value = "/profile/internal/get-user-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getProfileByUserId(@PathVariable("userId") Long userId);
}
