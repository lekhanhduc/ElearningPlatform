package vn.khanhduc.notificationservice.dto.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class Recipient implements Serializable {
    private String email;
    private String name;
}
