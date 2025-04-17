package vn.khanhduc.notificationservice.service;

import org.springframework.kafka.support.Acknowledgment;
import vn.khanhduc.event.dto.NotificationEvent;
import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;

public interface EmailService {
    EmailResponse sendEmail(SendEmailRequest request);
    EmailResponse sendMailWithKafka(NotificationEvent event);
    EmailResponse sendEmailPaymentSuccess (NotificationEvent event);
}
