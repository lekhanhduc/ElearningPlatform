package vn.khanhduc.paymentservice.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {
    private final ErrorCode errorCode;

    public PaymentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
