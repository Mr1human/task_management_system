package com.timur.taskmanagement.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .servers(List.of(
                                new Server()
                                        .url("http://localhost:8080")
                        )
                )
                .info(
                        new Info()
                                .title("Task Management API")
                                .version("1.0")
                                .description("API для управления задачами"));
    }
}
