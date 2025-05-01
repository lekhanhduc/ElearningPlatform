package vn.khanhduc.cartservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class CartCreationRequest implements Serializable {
    @NotBlank(message = "BookId cannot be blank")
    private String bookId;

    @NotBlank(message = "Book Title cannot be blank")
    private String bookTitle;

    @PositiveOrZero(message = "Price must be greater or equals 0")
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity must be greater than 0")
    private Integer quantity;
}
