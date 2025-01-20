package vn.khanhduc.identityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class UserCreationResponse implements Serializable {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
}
