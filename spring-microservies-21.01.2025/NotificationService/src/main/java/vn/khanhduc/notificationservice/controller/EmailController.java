package vn.khanhduc.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;
import vn.khanhduc.notificationservice.dto.response.ResponseData;
import vn.khanhduc.notificationservice.service.EmailService;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/brevo/send")
    ResponseData<EmailResponse> sendEmailWithBrevo(@RequestBody SendEmailRequest request) {
        return ResponseData.<EmailResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Send email successfully")
                .data(emailService.sendEmail(request))
                .build();
    }

}
