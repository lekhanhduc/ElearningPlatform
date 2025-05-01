package vn.khanhduc.identityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserProfileResponse  implements Serializable {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatar;
}
