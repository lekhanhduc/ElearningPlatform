package vn.khanhduc.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("profile-service", r -> r.path("/profile/api/v1/**")
                        .uri("lb://PROFILE-SERVICE"))
                .route("identity-service", r -> r.path("/identity/api/v1/**")
                        .uri("lb://IDENTITY-SERVICE"))
                .route("notification-service", r -> r.path("/notification/**")
                        .uri("lb://NOTIFICATION-SERVICE"))
                .route("post-service", r -> r.path("/post/**")
                        .uri("lb://POST-SERVICE"))
                .route("book-service", r -> r.path("/book/**")
                        .uri("lb://BOOK-SERVICE"))
                .route("search-service", r -> r.path("/search/**")
                        .uri("lb://SEARCH-SERVICE"))
                .build();
    }

}

