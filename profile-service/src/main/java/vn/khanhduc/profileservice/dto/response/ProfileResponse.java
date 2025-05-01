package vn.khanhduc.profileservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse implements Serializable {
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String avatar;
}
