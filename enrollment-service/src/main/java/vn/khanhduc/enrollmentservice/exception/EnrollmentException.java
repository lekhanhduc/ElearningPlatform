package vn.khanhduc.enrollmentservice.exception;

import lombok.Getter;

@Getter
public class EnrollmentException extends RuntimeException {

    private final ErrorCode errorCode;

    public EnrollmentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
