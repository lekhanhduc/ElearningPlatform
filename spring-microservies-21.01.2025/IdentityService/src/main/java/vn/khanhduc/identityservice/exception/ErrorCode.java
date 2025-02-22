package vn.khanhduc.identityservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_EXISTED(400, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(400, "User not existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DINED(403, "Access denied", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(400, "Token invalid", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID(400, "Refresh token invalid", HttpStatus.BAD_REQUEST),
    TOKEN_BLACK_LIST(400, "Token blacklist", HttpStatus.BAD_REQUEST),
    SIGN_OUT_FAILED(401, "Sign out failed", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EMPTY(400, "Access token is empty", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(400, "Token expired", HttpStatus.BAD_REQUEST),
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
