package vn.khanhduc.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.khanhduc.orderservice.dto.request.OrderCreationRequest;
import vn.khanhduc.orderservice.dto.response.OrderCreationResponse;
import vn.khanhduc.orderservice.dto.response.ResponseData;
import vn.khanhduc.orderservice.service.OrderService;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/creation")
    public CompletableFuture<ResponseData<OrderCreationResponse>> createOrder(@RequestBody OrderCreationRequest request) {
        return orderService.createOrder(request)
                .thenApply(result -> ResponseData.<OrderCreationResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(result)
                        .build());
    }

}
