package vn.khanhduc.notificationservice.dto.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class Sender implements Serializable {
    private String name;
    private String email;
}
