package vn.khanhduc.chatservice.repository.httpclient;

import vn.khanhduc.chatservice.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROFILE-SERVICE")
public interface ProfileClient {

    @GetMapping("/profile/internal/get-users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable Long userId);

}
