package vn.khanhduc.orderservice.dto.request;

import lombok.Getter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
public class OrderCreationRequest implements Serializable {
    private List<OrderDetailRequest> orderDetails;

    @Getter
    public static class OrderDetailRequest implements Serializable{
        private String bookId;
        private String bookName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

}
