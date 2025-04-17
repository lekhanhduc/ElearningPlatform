package vn.khanhduc.apigateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import vn.khanhduc.apigateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguration {

    @Value("${identity-service.base-url}")
    private String IDENTITY_SERVICE_BASE_URL;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(IDENTITY_SERVICE_BASE_URL)
                .build();
    }

    @Bean
    public IdentityClient identityClient() {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient());
        return HttpServiceProxyFactory.builderFor(webClientAdapter)
                .build()
                .createClient(IdentityClient.class);
    }

}
