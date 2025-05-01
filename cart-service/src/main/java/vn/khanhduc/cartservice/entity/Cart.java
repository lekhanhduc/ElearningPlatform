package vn.khanhduc.cartservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long userId;

    private String bookId;

    private String bookTitle;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

}
