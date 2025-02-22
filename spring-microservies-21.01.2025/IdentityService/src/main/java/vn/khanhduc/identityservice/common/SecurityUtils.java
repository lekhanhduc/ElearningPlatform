package vn.khanhduc.identityservice.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserLogin() {
        return Optional.of(extractUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
    private static String extractUsername(Authentication authentication) {
        if(authentication == null) return null;
        if(authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } if(authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } if(authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}
