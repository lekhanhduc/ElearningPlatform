package vn.khanhduc.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.khanhduc.identityservice.dto.request.ProfileCreateRequest;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;

//@FeignClient(value = "API-Client", url = "http://localhost:8081")
@FeignClient(name = "PROFILE-SERVICE")
public interface ProfileClient {
    @GetMapping("/profile/api/v1/profiles/{id}")
    UserProfileResponse getUserProfile(@PathVariable("id") String id);

    @PostMapping(value = "/profile/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreateRequest request);
}
