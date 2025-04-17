package vn.khanhduc.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailResponse implements Serializable {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
}
