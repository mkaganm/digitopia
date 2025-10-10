package com.digitopia.userservice.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("User Service API")
                .version("v1")
                .description("Endpoints for managing users"));
    }
}
