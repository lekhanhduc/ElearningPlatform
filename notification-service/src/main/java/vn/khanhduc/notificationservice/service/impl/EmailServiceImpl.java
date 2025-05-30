package vn.khanhduc.notificationservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.khanhduc.event.dto.NotificationEvent;
import vn.khanhduc.notificationservice.dto.request.EmailRequest;
import vn.khanhduc.notificationservice.dto.request.Recipient;
import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.request.Sender;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;
import vn.khanhduc.notificationservice.exception.ErrorCode;
import vn.khanhduc.notificationservice.exception.NotificationException;
import vn.khanhduc.notificationservice.repository.EmailClient;
import vn.khanhduc.notificationservice.service.EmailService;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailServiceImpl implements EmailService {

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.from}")
    private String from;

    private final EmailClient emailClient;
    private final SpringTemplateEngine templateEngine;

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

    @KafkaListener(topics = "user-onboard-success", groupId = "notification-group")
    @Override
    public EmailResponse sendMailWithKafka(NotificationEvent event) {
        log.info("Send email user onboard success");

        Map<String, Object> param = event.getParam();
        String templateCode = event.getTemplateCode();

        Context context = new Context();
        context.setVariables(param);

        String htmlContent = templateEngine.process(templateCode, context);

        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .email(from)
                        .name("Book Store")
                        .build())
                .to(List.of(Recipient.builder()
                                .name(param.get("name").toString())
                                .email(event.getRecipient())
                        .build()))
                .subject(param.get("subject").toString())
                .htmlContent(htmlContent)
                .build();
        try {
            return emailClient.sendEmailWithBrevo(apiKey, emailRequest);
        }catch (FeignException e) {
            log.error("Send email with kafka failed {}",e.getMessage());
            throw new NotificationException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }

    @KafkaListener(topics = "payment-success", groupId = "payment-group")
    @Override
    public EmailResponse sendEmailPaymentSuccess (NotificationEvent event) {
        log.info("Send Email Payment Success");
        var param = event.getParam();
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .email(from)
                        .name("Book Store")
                        .build())
                .to(List.of(Recipient.builder()
                                .email(event.getRecipient())
                                .name(String.format("%s", param.get("fullName")))
                        .build()))
                .subject("Payment success")
                .htmlContent(String.format("Payment success for order %s", param.get("orderId")))
                .build();
        try {
            return emailClient.sendEmailWithBrevo(apiKey, emailRequest);
        }catch (FeignException e) {
            log.error("Send email payment success failed {}",e.getMessage());
            throw new NotificationException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }

}
