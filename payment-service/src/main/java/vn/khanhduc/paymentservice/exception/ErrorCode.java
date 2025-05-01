package vn.khanhduc.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DINED(403, "Access denied", HttpStatus.FORBIDDEN),
    PAYMENT_PENDING_ERROR(500, "Payment pending error", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_CODE_NOT_EXISTED(400, "Order code not existed", HttpStatus.BAD_REQUEST),
    PAYMENT_ERROR(500, "Payment error", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
