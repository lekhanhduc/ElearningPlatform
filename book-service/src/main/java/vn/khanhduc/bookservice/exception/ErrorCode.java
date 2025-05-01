package vn.khanhduc.bookservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    FEIGN_ERROR(500, "Feign client error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DINED(403, "Access denied", HttpStatus.FORBIDDEN),
    BOOK_EXISTED(400, "Book already existed", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(404, "Book not found", HttpStatus.NOT_FOUND),
    SAVE_ELASTIC_SEARCH_ERROR(500, "Save book to elastic search error", HttpStatus.INTERNAL_SERVER_ERROR)
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
