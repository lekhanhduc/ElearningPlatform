package vn.khanhduc.paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import vn.khanhduc.paymentservice.dto.request.CreatePaymentLinkRequestBody;
import vn.khanhduc.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j(topic = "PAYMENT-CONTROLLER")
public class OrderController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    ObjectNode createPaymentLink(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        return paymentService.createPaymentLink(requestBody);
    }

    @GetMapping( "/{orderId}")
    ObjectNode getOrderById(@PathVariable("orderId") Long orderId) {
        return paymentService.getOrderById(orderId);
    }

    @PostMapping("/confirm-webhook")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws Exception {
        return paymentService.payosTransferHandler(body);
    }

}
