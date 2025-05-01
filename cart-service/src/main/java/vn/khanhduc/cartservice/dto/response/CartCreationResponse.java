package vn.khanhduc.cartservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CartCreationResponse implements Serializable {
    private Long userId;
    private String bookId;
    private String bookTitle;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}
