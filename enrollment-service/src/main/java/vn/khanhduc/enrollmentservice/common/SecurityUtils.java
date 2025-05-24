package vn.khanhduc.enrollmentservice.common;

import org.springframework.security.core.context.SecurityContextHolder;
import vn.khanhduc.enrollmentservice.exception.EnrollmentException;
import vn.khanhduc.enrollmentservice.exception.ErrorCode;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Long getCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new EnrollmentException(ErrorCode.UNAUTHENTICATED);
        return Long.valueOf(principal.get());
    }
}
