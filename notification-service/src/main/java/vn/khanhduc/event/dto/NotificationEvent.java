package vn.khanhduc.event.dto;

import lombok.*;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationEvent implements Serializable {
    private String channel; // Email, SMS, Zalo, ...
    private String recipient;
    private String templateCode;
    private Map<String, Object> param;
}
