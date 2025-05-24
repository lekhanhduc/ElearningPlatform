package vn.khanhduc.identityservice.common;

import org.springframework.security.core.context.SecurityContextHolder;
import vn.khanhduc.identityservice.exception.AuthenticationException;
import vn.khanhduc.identityservice.exception.ErrorCode;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Long getCurrentUserLogin() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) throw new AuthenticationException(ErrorCode.UNAUTHENTICATED);
        return Long.parseLong(principal.get());
    }

}
