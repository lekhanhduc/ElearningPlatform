package vn.khanhduc.identityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
public class SignInResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private Set<String> userType;
}
