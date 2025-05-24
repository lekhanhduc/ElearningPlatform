package vn.khanhduc.courseservice.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = null;
        var principal = SecurityContextHolder.getContext().getAuthentication();
        if(principal instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            token = jwtAuthenticationToken.getToken().getTokenValue();
        }

        if(token == null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attributes != null) {
                token = attributes.getRequest().getHeader("Authorization");
            }
        }

        if(token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
