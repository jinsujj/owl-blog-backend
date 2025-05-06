package me.blog.backend.common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .servers(Arrays.asList(
                    new Server()
                            .url("http://localhost:8080")
                            .description("Local Development Server"),
                    new Server()
                            .url("https://backend.owl-dev.me")
                            .description("Production Server")
            ))
            .info(new Info()
                    .title("Owl Blog Backend API")
                    .version("1.0.0")
            );
  }
}