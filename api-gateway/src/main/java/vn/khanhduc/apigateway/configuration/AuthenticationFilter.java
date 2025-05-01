package vn.khanhduc.apigateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vn.khanhduc.apigateway.dto.response.ResponseData;
import vn.khanhduc.apigateway.service.IdentityService;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "GLOBAL-FILTER")
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final String[] WHILE_LIST = {
            // .* vì dùng matches nó yêu cầu biểu thức regex
            "/identity/api/v1/auth/.*",
            "/identity/api/v1/users/registration",
            "/notification/brevo/send",
            "/book/fetch-all/.*",
            "/book/find-all-with-querydsl",
            "/book/detail/.*",
            "/file/download/.*",
            "/search/.*",
            "/identity/oauth2/authorization/google",
            "/identity/oauth2/authorization/github",
            "/[a-zA-Z0-9_-]+/v3/api-docs(/.*)?",
            "/swagger-ui/.*",
            "/swagger-ui.html",
            "/webjars/.*",
            "/swagger-resources/.*",
            "/profile/api/v1/v3/api-docs"
    };

    private final IdentityService identityService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationFilter-API GATEWAY {}", exchange.getRequest().getPath());
        // 1. Check While List --> Public endpoint
        if(isWhileList(exchange.getRequest())) {
            return chain.filter(exchange);
        }
        // 2. Get token from Authorization Header
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeaders))
            return unauthenticated(exchange.getResponse());

        String token = authHeaders.getFirst().replace("Bearer ", "");
        // 3. Verify Token -> Call IdentityService to authentication -> delegated identity service
        return identityService.verificationToken(token).flatMap(verificationResponse -> {
            if(verificationResponse.getData().isValid()) {
                log.info("verification token {}", verificationResponse.getData());
                return chain.filter(exchange);
            } else {
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume((throwable) -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isWhileList(ServerHttpRequest request) {
        return Arrays.stream(WHILE_LIST)
                .anyMatch(s -> request.getURI().getPath().matches(s)); // hoặc dùng equals thay matches
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ResponseData<Object> responseData = new ResponseData<>();
        responseData.setCode(HttpStatus.UNAUTHORIZED.value());
        responseData.setMessage("Unauthenticated");
        try {
            return response.writeWith(Mono.just(response.bufferFactory()
                    .wrap(objectMapper.writeValueAsString(responseData)
                            .getBytes(StandardCharsets.UTF_8))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
