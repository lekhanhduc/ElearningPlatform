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

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/creation")
    ResponseData<OrderCreationResponse> createOrder(@RequestBody OrderCreationRequest request) {
        var result = orderService.createOrder(request);

        return ResponseData.<OrderCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .data(result)
                .build();
    }

}
