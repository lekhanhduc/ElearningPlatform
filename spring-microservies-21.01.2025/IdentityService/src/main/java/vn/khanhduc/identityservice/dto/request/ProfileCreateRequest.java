package vn.khanhduc.identityservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class ProfileCreateRequest implements Serializable {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
