package vn.khanhduc.paymentservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse implements Serializable {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
}

