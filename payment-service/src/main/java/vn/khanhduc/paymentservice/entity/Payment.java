package vn.khanhduc.paymentservice.entity;

import vn.khanhduc.paymentservice.common.Currency;
import vn.khanhduc.paymentservice.common.PaymentGateWay;
import vn.khanhduc.paymentservice.common.PaymentMethod;
import vn.khanhduc.paymentservice.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Payment")
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "order_code", nullable = false)
    private Long orderCode;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "payment_gate_way", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateWay paymentGateWay;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

}
