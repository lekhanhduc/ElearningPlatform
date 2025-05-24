package vn.khanhduc.courseservice.common;

import org.springframework.security.core.context.SecurityContextHolder;
import vn.khanhduc.courseservice.exception.CourseException;
import vn.khanhduc.courseservice.exception.ErrorCode;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Long getCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new CourseException(ErrorCode.UNAUTHENTICATED);
        return Long.valueOf(principal.get());
    }
}
