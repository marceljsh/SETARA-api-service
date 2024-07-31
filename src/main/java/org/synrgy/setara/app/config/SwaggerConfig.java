package org.synrgy.setara.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

  @Value("${openapi.dev-url}")
  private String devUrl;

  @Value("${openapi.prod-url}")
  private String prodUrl;

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public-api")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(apiInfo())
        .servers(apiServers())
        .components(apiComponents())
        .addSecurityItem(apiSecurityRequirement());
  }

  private Info apiInfo() {
    return new Info()
        .title("Setara API")
        .version("1.0")
        .contact(new Contact()
            .name("Setara")
            .url("https://setara.com")
            .email("setara@binar.co.id"))
        .description("This API exposes endpoints to manage Setara")
        .termsOfService("https://setara.com/terms")
        .license(new License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/"));
  }

  private List<Server> apiServers() {
    return List.of(
        new Server()
            .url(devUrl)
            .description("Server URL in Development environment"),
        new Server()
            .url(prodUrl)
            .description("Server URL in Production environment"));
  }

  private Components apiComponents() {
    return new Components()
        .addSecuritySchemes("bearerAuth", new SecurityScheme()
            .name("Bearer Authentication")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT"));
  }

  private SecurityRequirement apiSecurityRequirement() {
    return new SecurityRequirement().addList("bearerAuth");
  }
}
