package vn.khanhduc.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orders_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class OrderDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice; // Giá của một quyển sách

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal; // Tổng giá tiền cho sách này (quantity * unitPrice)

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
