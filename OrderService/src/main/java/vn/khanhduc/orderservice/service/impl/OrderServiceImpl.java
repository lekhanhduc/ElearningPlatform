package vn.khanhduc.orderservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.khanhduc.event.dto.PaymentEvent;
import vn.khanhduc.orderservice.common.OrderStatus;
import vn.khanhduc.orderservice.common.PaymentMethod;
import vn.khanhduc.orderservice.dto.request.OrderCreationRequest;
import vn.khanhduc.orderservice.dto.response.OrderCreationResponse;
import vn.khanhduc.orderservice.entity.Order;
import vn.khanhduc.orderservice.entity.OrderDetail;
import vn.khanhduc.orderservice.exception.ErrorCode;
import vn.khanhduc.orderservice.exception.OrderException;
import vn.khanhduc.orderservice.repository.OrderRepository;
import vn.khanhduc.orderservice.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-SERVICE")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @CircuitBreaker(name = "PAYMENT-SERVICE", fallbackMethod = "createOrderFallback")
    @Retry(name = "PAYMENT-SERVICE")
    @TimeLimiter(name = "PAYMENT-SERVICE")
    @Override
    public CompletableFuture<OrderCreationResponse> createOrder(OrderCreationRequest request) {
        log.info("Create order");

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new OrderException(ErrorCode.UNAUTHENTICATED);

        Long userId = Long.valueOf(authentication.getName());

        BigDecimal totalPrice = request.getOrderDetails()
                .stream().map(total -> {
                    Integer quantity = total.getQuantity();
                    BigDecimal unitPrice = total.getUnitPrice();
                    return unitPrice.multiply(BigDecimal.valueOf(quantity));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        String currentTimeString = String.valueOf(new Date().getTime());
        Long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
        Order order = Order.builder()
                .userId(userId)
                .orderCode(orderCode)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .paymentMethod(PaymentMethod.ONLINE_BANKING)
                .orderDate(LocalDateTime.now())
                .build();

        // Lưu order trước khi lưu orderDetail --> Có order mới có order detail
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderRequest -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .userId(userId)
                    .bookId(orderRequest.getBookId())
                    .bookName(orderRequest.getBookName())
                    .quantity(orderRequest.getQuantity())
                    .unitPrice(orderRequest.getUnitPrice())
                    .subtotal(orderRequest.getUnitPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                    .order(order)
                    .build();
            log.info("Order detail created");
            return orderDetail;
        }).toList();

        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .userId(userId)
                .orderId(order.getId())
                .orderCode(orderCode)
                .totalPrice(totalPrice)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            kafkaTemplate.send("order-pending", paymentEvent);
            log.info("Kafka message sent for orderCode: {}", orderCode);
            return OrderCreationResponse.builder()
                    .orderId(order.getId())
                    .orderCode(orderCode)
                    .userId(userId)
                    .orderDetails(order.getOrderDetails().stream().map(o -> OrderCreationResponse.OrderDetailResponse.builder()
                                    .bookId(o.getBookId())
                                    .quantity(o.getQuantity())
                                    .unitPrice(o.getUnitPrice())
                                    .subtotal(o.getSubtotal())
                                    .paymentMethod(PaymentMethod.ONLINE_BANKING)
                                    .orderStatus(OrderStatus.PENDING)
                                    .build())
                            .toList())
                    .totalPrice(totalPrice)
                    .orderDate(order.getOrderDate())
                    .build();
        }).exceptionally(throwable -> {
            log.error("Error in CompletableFuture: {}", throwable.getMessage());
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            throw new OrderException(ErrorCode.ORDER_CREATION_ERROR);
        });
    }

    public CompletableFuture<OrderCreationResponse> createOrderFallback(OrderCreationRequest request, Throwable throwable) {
        log.error("Fallback triggered for createOrder due to: {}", throwable.getMessage());
        throw new OrderException(ErrorCode.PAYMENT_SERVICE_UNAVAILABLE);
    }

}
