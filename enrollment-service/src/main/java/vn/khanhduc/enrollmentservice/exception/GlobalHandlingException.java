package vn.khanhduc.enrollmentservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.khanhduc.enrollmentservice.dto.response.ErrorResponse;
import java.util.Date;

@RestControllerAdvice
public class GlobalHandlingException {

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleEnrollmentException(EnrollmentException exception, HttpServletRequest request) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(new Date())
                .status(errorCode.getCode())
                .error(errorCode.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
}
