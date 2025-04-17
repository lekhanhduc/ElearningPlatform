package vn.khanhduc.apigateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import vn.khanhduc.apigateway.dto.response.ErrorResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class RateLimiterGatewayFilterOneService implements GatewayFilter {

    private final RateLimiterRegistry rateLimiterRegistry;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("identityServiceRateLimiter");
        boolean permitted = rateLimiter.acquirePermission();
        if (!permitted) {
            return rateLimitError(exchange);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> rateLimitError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.TOO_MANY_REQUESTS.value())
                .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                .message("Too many requests, please try again")
                .path(exchange.getRequest().getURI().getPath())
                .timestamp(new Date())
                .build();

        try {
            byte[] responseBytes = objectMapper.writeValueAsString(error).getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));

        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Error serializing rate limit response", e));
        }
    }

}
