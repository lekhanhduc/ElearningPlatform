package vn.khanhduc.notificationservice.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class SendEmailRequest implements Serializable {
    private Recipient to;
    private String subject;
    private String htmlContent;
}
