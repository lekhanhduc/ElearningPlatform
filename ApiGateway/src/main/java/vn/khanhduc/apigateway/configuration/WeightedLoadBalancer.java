package vn.khanhduc.apigateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.Random;

@Slf4j
public class WeightedLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ServiceInstanceListSupplier serviceInstanceListSupplier;
    private final Random random = new Random();

    public WeightedLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier) {
        this.serviceInstanceListSupplier = serviceInstanceListSupplier;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        return serviceInstanceListSupplier.get()
                .next()
                .map(serviceInstances -> {
                    if(serviceInstances == null || serviceInstances.isEmpty()) {
                        log.warn("No service instances found");
                        return new DefaultResponse(null);
                    }
                    log.info("Choose service instance");

                    int totalWeight = serviceInstances.stream()
                            .mapToInt(this::getInstanceWeight)
                            .sum();

                    if(totalWeight <= 0) {
                        log.debug("Total weight is 0 or negative, selecting first instance");
                        return new DefaultResponse(serviceInstances.getFirst());
                    }

                    int randomIndex = random.nextInt(totalWeight);

                    int currentWeight = 0;

                    for (ServiceInstance instance : serviceInstances) {
                        int instanceWeight = getInstanceWeight(instance);
                        currentWeight += instanceWeight;
                        if (randomIndex < currentWeight) {
                            return new DefaultResponse(instance);
                        }
                    }
                    return new DefaultResponse(serviceInstances.getFirst());
                });
    }

    @Override
    public Mono<Response<ServiceInstance>> choose() {
        return ReactorServiceInstanceLoadBalancer.super.choose();
    }

    private int getInstanceWeight(ServiceInstance instance) {
        int DEFAULT_WEIGHT = 1;
        try {
           Map<String, String> metadata = instance.getMetadata();
           if(metadata.isEmpty()) {
               return DEFAULT_WEIGHT;
           }

           String weightStr = metadata.get("weight");
           if(weightStr == null) {
               return DEFAULT_WEIGHT;
           }

           int weight = Integer.parseInt(weightStr);
           log.info("Weight is {}", weight);
           return weight > 0 ? weight : DEFAULT_WEIGHT;
       }catch (NumberFormatException  e) {
           return DEFAULT_WEIGHT;
       }
    }

}
