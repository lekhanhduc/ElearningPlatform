package vn.khanhduc.notificationservice.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.khanhduc.notificationservice.dto.request.EmailRequest;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;

@FeignClient(name = "EMAIL-SERVICE", url = "https://api.brevo.com")
public interface EmailClient {

    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmailWithBrevo(@RequestHeader(name = "api-key") String apiKey,
                                     @RequestBody EmailRequest request);
}
