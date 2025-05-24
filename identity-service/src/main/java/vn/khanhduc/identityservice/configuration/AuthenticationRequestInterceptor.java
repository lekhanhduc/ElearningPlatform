package vn.khanhduc.identityservice.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Component
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = null;
        var principal = SecurityContextHolder.getContext().getAuthentication();
        if(principal instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            token = jwtAuthenticationToken.getToken().getTokenValue();
        }
        if(token == null) {
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null) {
                token = requestAttributes.getRequest().getHeader("Authorization");
            }
        }
        if (token != null) {
            requestTemplate.header("Authorization",  "Bearer " + token);
        }
    }

}
