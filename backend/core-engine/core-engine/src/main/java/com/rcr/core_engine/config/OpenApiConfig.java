package com.rcr.core_engine.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration 
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // A single "/" server forces Swagger UI to send API calls relatively
        // to whatever domain/port you are currently viewing it from
        return new OpenAPI()
                .servers(List.of(new Server().url("/").description("Default Server URL")));
    }
}
