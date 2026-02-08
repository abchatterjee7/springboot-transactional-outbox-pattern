package org.aadi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("Spring Boot Transactional Outbox Pattern - Inventory Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Inventory Service Team")
                                .email("inventory-service@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:9192").description("Local Server"),
                        new Server().url("http://inventory-service:9192").description("Docker Server")
                ));
    }
}
