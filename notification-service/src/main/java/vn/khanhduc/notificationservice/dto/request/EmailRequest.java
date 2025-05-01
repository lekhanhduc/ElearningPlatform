package vn.khanhduc.notificationservice.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest implements Serializable {
    private Sender sender;
    private List<Recipient> to;
    private String htmlContent;
    private String subject;
}
