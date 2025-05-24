package vn.khanhduc.paymentservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import vn.khanhduc.event.dto.PaymentEvent;
import vn.khanhduc.paymentservice.common.Currency;
import vn.khanhduc.paymentservice.common.PaymentGateWay;
import vn.khanhduc.paymentservice.common.PaymentMethod;
import vn.khanhduc.paymentservice.common.PaymentStatus;
import vn.khanhduc.paymentservice.entity.Payment;
import vn.khanhduc.paymentservice.exception.ErrorCode;
import vn.khanhduc.paymentservice.exception.PaymentException;
import vn.khanhduc.paymentservice.repository.PaymentRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PAYMENT-CONSUMER")
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "order-pending", groupId = "payment-group")
    public void orderPending(PaymentEvent event, Acknowledgment acknowledgment) {
        log.info("Order Pending {}", event.toString());
        try {
            Payment payment = Payment.builder()
                    .orderId(event.getOrderId())
                    .orderCode(event.getOrderCode())
                    .userId(event.getUserId())
                    .totalPrice(event.getTotalPrice())
                    .paymentMethod(PaymentMethod.ONLINE_BANKING)
                    .currency(Currency.VND)
                    .paymentGateWay(PaymentGateWay.PAYOS)
                    .paymentStatus(PaymentStatus.PENDING)
                    .createdDate(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
            acknowledgment.acknowledge();
            log.info("Order Pending --> Payment saved successfully");
        }catch (Exception e) {
            log.error("Error when save payment: {}", e.getMessage());
            throw new PaymentException(ErrorCode.PAYMENT_PENDING_ERROR);
        }
    }

    @KafkaListener(topics = "enrollment-pending", groupId = "payment-group")
    public void enrollmentPending(PaymentEvent event, Acknowledgment acknowledgment) {
        log.info("Enrollment Pending");
    }
}
