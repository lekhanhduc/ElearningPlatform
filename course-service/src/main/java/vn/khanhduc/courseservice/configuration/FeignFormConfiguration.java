package vn.khanhduc.courseservice.configuration;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignFormConfiguration {

    @Bean
    public Encoder encoder() {
        return new SpringFormEncoder();
    }

}
