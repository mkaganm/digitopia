package com.digitopia.orgservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Simple blocking HTTP client; perfect for MVP
@Configuration
public class HttpClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // add timeouts later if needed
    }
}
