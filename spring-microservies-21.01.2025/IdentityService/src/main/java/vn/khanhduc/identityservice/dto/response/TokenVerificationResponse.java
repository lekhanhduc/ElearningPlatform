package vn.khanhduc.identityservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class TokenVerificationResponse implements Serializable {
    private boolean isValid;
}
