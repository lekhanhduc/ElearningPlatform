package vn.khanhduc.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class CreatePaymentLinkRequestBody implements Serializable {
    private String productName;
    private String description;
    private String returnUrl;
    private int price;
    private String cancelUrl;
    private int quantity;
    private Long orderCode;
}
