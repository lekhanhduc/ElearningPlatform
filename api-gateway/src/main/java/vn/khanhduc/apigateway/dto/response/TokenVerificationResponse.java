package vn.khanhduc.apigateway.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenVerificationResponse implements Serializable {
    private boolean isValid;
}
