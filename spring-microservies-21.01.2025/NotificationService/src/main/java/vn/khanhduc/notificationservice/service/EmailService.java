package vn.khanhduc.notificationservice.service;

import vn.khanhduc.notificationservice.dto.request.SendEmailRequest;
import vn.khanhduc.notificationservice.dto.response.EmailResponse;

public interface EmailService {
    EmailResponse sendEmail(SendEmailRequest request);
}
