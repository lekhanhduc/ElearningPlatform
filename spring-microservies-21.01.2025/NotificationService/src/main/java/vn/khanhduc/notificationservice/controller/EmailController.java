package vn.khanhduc.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;
import vn.khanhduc.notificationservice.dto.response.ResponseData;
import vn.khanhduc.notificationservice.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
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

//    @KafkaListener(topics = "user-onboard-success", groupId = "notification-group")
//    public void listener (String message) {
//        log.info(message);
//    }

}
