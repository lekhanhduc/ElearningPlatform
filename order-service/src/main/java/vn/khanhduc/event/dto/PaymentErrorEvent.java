package vn.khanhduc.event.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentErrorEvent implements Serializable {
    private String orderId;
}
