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
                        .title("Order Poller Service API")
                        .description("Spring Boot Transactional Outbox Pattern - Order Poller Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Order Poller Service Team")
                                .email("order-poller-service@example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:9292").description("Local Server"),
                        new Server().url("http://order-poller:9292").description("Docker Server")
                ));
    }
}
