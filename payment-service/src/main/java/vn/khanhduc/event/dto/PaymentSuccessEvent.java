package vn.khanhduc.event.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessEvent implements Serializable {
    private String orderId;
    private Long userId;
    private Long orderCode;
}
