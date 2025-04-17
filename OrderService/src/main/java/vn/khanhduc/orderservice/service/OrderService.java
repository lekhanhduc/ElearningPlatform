package vn.khanhduc.orderservice.service;

import vn.khanhduc.orderservice.dto.request.OrderCreationRequest;
import vn.khanhduc.orderservice.dto.response.OrderCreationResponse;

public interface OrderService {
    OrderCreationResponse createOrder(OrderCreationRequest request);
}
