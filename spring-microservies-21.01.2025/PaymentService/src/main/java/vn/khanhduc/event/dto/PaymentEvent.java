package vn.khanhduc.event.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEvent implements Serializable {
    private Long userId;
    private String orderId;
    private Long orderCode;
    private BigDecimal totalPrice;
}
