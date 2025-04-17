package vn.khanhduc.orderservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.khanhduc.orderservice.common.OrderStatus;
import vn.khanhduc.orderservice.common.PaymentMethod;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderCreationResponse implements Serializable {
    private String orderId;
    private Long orderCode;
    private Long userId;
    private List<OrderDetailResponse> orderDetails;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;

    @Getter
    @Setter
    @Builder
    public static class OrderDetailResponse implements Serializable {
        private String bookId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private PaymentMethod paymentMethod;
        private OrderStatus orderStatus;
    }
}
