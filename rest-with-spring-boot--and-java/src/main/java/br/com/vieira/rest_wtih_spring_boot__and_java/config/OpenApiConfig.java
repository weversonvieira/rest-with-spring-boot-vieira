package br.com.vieira.rest_wtih_spring_boot__and_java.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                .title("REST API'S RESTful fom 0 with Java, Spring Boot, Kuberneter and Docker")
                .version("v1")
                .description("REST API'S RESTful fom 0 with Java, Spring Boot, Kuberneter and Docker")
                .termsOfService("https://pub.erudio.com.br/meus-cursos").license(new License()
                        .name("Apache 2.0")
                        .url("https://pub.erudio.com.br/meus-cursos")));
    }
}
