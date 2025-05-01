package vn.khanhduc.chatservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChannelInterceptorConfiguration implements ChannelInterceptor {

    private final JwtDecoderCustomizer jwtDecoder;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("Processing STOMP command: {}", accessor != null ? accessor.getCommand() : "null");
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if(token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var authority = jwtDecoder.decode(token).getClaimAsString("authority");
            List<GrantedAuthority> authorities = authority != null
                    ? List.of(new SimpleGrantedAuthority(authority)) :
                    Collections.emptyList();
            Long userId = Long.parseLong(jwtDecoder.decode(token).getSubject());
            Authentication authentication = new PreAuthenticatedAuthenticationToken(
                    userId,
                    null,
                    authorities
            );
            accessor.setUser(authentication);
        }
        return message;
    }

}
