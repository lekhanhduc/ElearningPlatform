package vn.khanhduc.apigateway.configuration;

import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomLoadBalancerConfiguration {

    @Bean
    public ReactorServiceInstanceLoadBalancer reactorServiceInstanceLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier) {
        return new WeightedLoadBalancer(serviceInstanceListSupplier);
    }

    @Bean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()  // Sử dụng Discovery Client để lấy các instance
                .withWeighted()  // Sử dụng trọng số từ metadata của instance
                .withCaching()  // Cache kết quả để tối ưu hiệu suất
                .build(context);
    }

}

