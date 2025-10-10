package com.digitopia.orgservice.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI orgOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Organization Service API")
                .version("v1")
                .description("Endpoints for managing organizations"));
    }
}
