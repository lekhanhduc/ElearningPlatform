package vn.khanhduc.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import vn.khanhduc.event.dto.PaymentErrorEvent;
import vn.khanhduc.orderservice.entity.Order;
import vn.khanhduc.orderservice.exception.ErrorCode;
import vn.khanhduc.orderservice.exception.OrderException;
import vn.khanhduc.orderservice.repository.OrderRepository;

@Service
@Slf4j(topic = "ORDER-ROLLBACK-SERVICE")
@RequiredArgsConstructor
public class OrderRollbackConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-error", groupId = "order-service")
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000, multiplier = 2),
            label = "payment-error")
    public void handlePaymentError(PaymentErrorEvent event, Acknowledgment acknowledgment) {
        if(event == null || event.getOrderId() == null) {
            log.warn("Order id is null");
            return;
        }

        log.error("Payment error with order id {}", event.getOrderId());

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
        orderRepository.deleteById(order.getId());
        log.info("Order deleted");

        acknowledgment.acknowledge();
    }

}
