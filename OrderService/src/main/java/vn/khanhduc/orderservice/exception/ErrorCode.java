package vn.khanhduc.orderservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DINED(403, "Access denied", HttpStatus.FORBIDDEN),
    ORDER_NOT_FOUND(404, "Order not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_CREATION_ERROR(500, "Order creation error, please try again later.", HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_SERVICE_UNAVAILABLE(503, "Không thể tạo đơn hàng do lỗi hệ thống thanh toán. Vui lòng thử lại sau.", HttpStatus.SERVICE_UNAVAILABLE)
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
