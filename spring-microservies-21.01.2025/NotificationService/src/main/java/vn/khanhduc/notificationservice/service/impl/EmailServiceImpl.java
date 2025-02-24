package vn.khanhduc.notificationservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.khanhduc.notificationservice.dto.request.EmailRequest;
import vn.khanhduc.notificationservice.dto.request.Recipient;
import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.request.Sender;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;
import vn.khanhduc.notificationservice.exception.ErrorCode;
import vn.khanhduc.notificationservice.exception.NotificationException;
import vn.khanhduc.notificationservice.repository.httpclient.EmailClient;
import vn.khanhduc.notificationservice.service.EmailService;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.from}")
    private String from;

    private final EmailClient emailClient;

    @Override
    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = new EmailRequest();
        Sender sender = Sender.builder()
                .email(from)
                .name("Book Store")
                .build();

        List<Recipient> recipient = List.of(Recipient.builder()
                        .email(request.getTo().getEmail())
                .build());

        emailRequest.setSender(sender);
        emailRequest.setTo(recipient);
        emailRequest.setSubject(request.getSubject());
        emailRequest.setHtmlContent(request.getHtmlContent());
        try {
            return emailClient.sendEmailWithBrevo(apiKey, emailRequest);
        }catch (FeignException e) {
            log.error("Send email with brevo failed {}",e.getMessage());
            throw new NotificationException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
