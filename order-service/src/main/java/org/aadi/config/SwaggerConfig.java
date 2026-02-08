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
                        .title("Order Service API")
                        .description("Spring Boot Transactional Outbox Pattern - Order Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Order Service Team")
                                .email("order-service@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:9191").description("Local Server"),
                        new Server().url("http://order-service:9191").description("Docker Server")
                ));
    }
}
