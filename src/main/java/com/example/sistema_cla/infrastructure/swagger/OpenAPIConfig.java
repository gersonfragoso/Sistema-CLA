package com.example.sistema_cla.infrastructure.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema CLA API")
                        .description("API para gerenciamento de locais acessíveis e avaliações")
                        .version("1.0.0"));
    }
}