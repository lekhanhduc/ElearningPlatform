package vn.khanhduc.identityservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.khanhduc.identityservice.configuration.AuthenticationRequestInterceptor;
import vn.khanhduc.identityservice.dto.request.ProfileCreateRequest;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;

//@FeignClient(value = "API-Client", url = "http://localhost:8081")
@FeignClient(name = "PROFILE-SERVICE", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping("/profile/internal/users/{id}")
    UserProfileResponse getUserProfile(@PathVariable("id") Long id);

//    @PostMapping(value = "/profile/api/v1/internal/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
//    UserProfileResponse createProfile(
//            @RequestHeader(name = "Authorization") String authorization,
//            @RequestBody ProfileCreateRequest request);

    @PostMapping(value = "/profile/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createProfile(@RequestBody ProfileCreateRequest request);

    @GetMapping("/profile/api/v1/profiles")
    UserProfileResponse getAllProfile(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(name = "page", required = false, defaultValue = "1") int size);
}
