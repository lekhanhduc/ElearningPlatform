package vn.khanhduc.apigateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import vn.khanhduc.apigateway.dto.response.ErrorResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static vn.khanhduc.apigateway.constant.RateLimiterName.IDENTITY_SERVICE;
import static vn.khanhduc.apigateway.constant.RateLimiterName.PROFILE_SERVICE;

@Configuration
@RequiredArgsConstructor
public class GatewayConfiguration {

    private final RateLimiterRegistry rateLimiterRegistry;
    private final ObjectMapper objectMapper;
    private final RouteDefinitionLocator routeDefinitionLocator;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("profile-service", r -> r.path("/profile/api/v1/**")
                        .filters(f -> f
                                .filter(createRateLimiterFilter(PROFILE_SERVICE)))
                        .uri("lb://PROFILE-SERVICE"))
                .route("identity-service", r -> r.path("/identity/**")
                        .filters(f -> f
                                .filter(createRateLimiterFilter(IDENTITY_SERVICE)))
                        .uri("lb://IDENTITY-SERVICE"))
                .route("notification-service", r -> r.path("/notification/**")
                        .uri("lb://NOTIFICATION-SERVICE"))
                .route("post-service", r -> r.path("/post/**")
                        .uri("lb://POST-SERVICE"))
                .route("book-service", r -> r.path("/book/**")
                        .uri("lb://BOOK-SERVICE"))
                .route("search-service", r -> r.path("/search/**")
                        .uri("lb://SEARCH-SERVICE"))
                .route("order-service", r -> r.path("/order/**")
                        .uri("lb://ORDER-SERVICE"))
                .route("payment-service", r -> r.path("/payment/**")
                        .uri("lb://PAYMENT-SERVICE"))
                .route("review-service", r -> r.path("/review/**")
                        .uri("lb://REVIEW-SERVICE"))
                .route("file-service", r -> r.path("/file/**")
                        .uri("lb://FILE-SERVICE"))
                .route("cart-service", r -> r.path("/cart/**")
                        .uri("lb://CART-SERVICE"))
                .route("chat-service", r -> r.path("/chat/**")
                        .uri("lb://CHAT-SERVICE"))
                .build();
    }

    private GatewayFilter createRateLimiterFilter(String rateLimiterName) {
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterName);

        return (exchange, chain) -> {
            boolean permitted = rateLimiter.acquirePermission();
            if(!permitted) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .code(HttpStatus.TOO_MANY_REQUESTS.value())
                        .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                        .message("Too many requests, please try again")
                        .path(exchange.getRequest().getURI().getPath())
                        .timestamp(new Date())
                        .build();

                try {
                    byte[] bytes = objectMapper.writeValueAsString(errorResponse).getBytes();
                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
                } catch (JsonProcessingException e) {
                    return Mono.error(new RuntimeException("Error serializing rate limit response", e));
                }
            }
            return chain.filter(exchange);
        };
    }


}

