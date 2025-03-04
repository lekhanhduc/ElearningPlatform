package vn.khanhduc.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import vn.khanhduc.event.dto.NotificationEvent;
import vn.khanhduc.paymentservice.common.Channel;
import vn.khanhduc.paymentservice.common.PaymentStatus;
import vn.khanhduc.paymentservice.dto.request.CreatePaymentLinkRequestBody;
import vn.khanhduc.paymentservice.exception.ErrorCode;
import vn.khanhduc.paymentservice.exception.PaymentException;
import vn.khanhduc.paymentservice.model.Payment;
import vn.khanhduc.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.khanhduc.paymentservice.repository.httpclient.UserClient;
import vn.payos.PayOS;
import vn.payos.type.*;
import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PAYMENT-SERVICE")
public class PaymentService {

    private final PayOS payOS;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserClient userClient;

    public ObjectNode createPaymentLink(CreatePaymentLinkRequestBody requestBody) {
        ObjectNode response = objectMapper.createObjectNode();
        final int quantity = requestBody.getQuantity();
        final String productName = requestBody.getProductName();
        final String description = requestBody.getDescription();
        final String returnUrl = requestBody.getReturnUrl();
        final String cancelUrl = requestBody.getCancelUrl();
        final int price = requestBody.getPrice() * quantity;

        long orderCode = requestBody.getOrderCode();
        ItemData item = ItemData.builder()
                .name(productName)
                .price(price)
                .quantity(quantity).build();

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .description(description)
                .amount(price)
                .item(item)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();
        try {
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;
        } catch (Exception e) {
            log.error("Error when create payment link: {}", e.getMessage());
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode payosTransferHandler(ObjectNode body) throws JsonProcessingException {
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
        try {
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);

            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            String code = data.getCode();
            Long orderCode = data.getOrderCode();
            Payment payment = paymentRepository.findByOrderCode(orderCode)
                    .orElseThrow(() -> new PaymentException(ErrorCode.ORDER_CODE_NOT_EXISTED));

            if(Objects.equals(code, "00")) {
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                var userDetail = userClient.getUserDetail(payment.getUserId());
                var param = new HashMap<String, Object>();
                param.put("orderId", orderCode);
                param.put("fullName", userDetail.getData().getFirstName() + " " + userDetail.getData().getLastName());
                NotificationEvent event = NotificationEvent.builder()
                        .channel(Channel.EMAIL)
                        .recipient(userDetail.getData().getEmail())
                        .param(param)
                        .build();
                kafkaTemplate.send("payment-success", event);
                log.info("Payment success");
            } else {
                payment.setPaymentStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);
            response.put("error", 0);
            response.put("message", "success");
            return response;

        } catch (Exception e) {
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode getOrderById(Long orderId) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error when get order by id: {}", e.getMessage());
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

}
