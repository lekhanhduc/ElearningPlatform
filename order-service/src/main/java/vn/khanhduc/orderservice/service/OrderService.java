package vn.khanhduc.orderservice.service;

import vn.khanhduc.orderservice.dto.request.OrderCreationRequest;
import vn.khanhduc.orderservice.dto.response.OrderCreationResponse;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderCreationResponse> createOrder(OrderCreationRequest request);
}
