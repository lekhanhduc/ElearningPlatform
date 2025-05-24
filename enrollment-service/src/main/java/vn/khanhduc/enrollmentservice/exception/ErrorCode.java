package vn.khanhduc.enrollmentservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNAUTHENTICATED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DINED(403, "Access denied", HttpStatus.FORBIDDEN),
    ENROLLMENT_EXISTED(409, "Enrollment already existed", HttpStatus.CONFLICT),
    COURSE_NOT_FOUND(404, "Course not found", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
