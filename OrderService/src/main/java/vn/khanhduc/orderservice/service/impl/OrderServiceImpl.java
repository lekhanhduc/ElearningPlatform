package vn.khanhduc.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.khanhduc.event.dto.PaymentEvent;
import vn.khanhduc.orderservice.common.OrderStatus;
import vn.khanhduc.orderservice.common.PaymentMethod;
import vn.khanhduc.orderservice.dto.request.OrderCreationRequest;
import vn.khanhduc.orderservice.dto.response.OrderCreationResponse;
import vn.khanhduc.orderservice.entity.Order;
import vn.khanhduc.orderservice.entity.OrderDetail;
import vn.khanhduc.orderservice.repository.OrderDetailRepository;
import vn.khanhduc.orderservice.repository.OrderRepository;
import vn.khanhduc.orderservice.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-SERVICE")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderCreationResponse createOrder(OrderCreationRequest request) {
        log.info("Create order");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
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
        orderRepository.save(order);
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

            orderDetailRepository.save(orderDetail);
            log.info("Order detail created");
            return orderDetail;
        }).toList();

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .userId(userId)
                .orderId(order.getId())
                .orderCode(orderCode)
                .totalPrice(totalPrice)
                .build();

        kafkaTemplate.send("order-pending", paymentEvent);

        return OrderCreationResponse.builder()
                .orderId(order.getId())
                .orderCode(orderCode)
                .userId(userId)
                .orderDetails(orderDetails.stream().map(o -> OrderCreationResponse.OrderDetailResponse.builder()
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
    }

}
