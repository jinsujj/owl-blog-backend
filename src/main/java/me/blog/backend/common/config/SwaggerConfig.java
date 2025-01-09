package me.blog.backend.common.config;

import java.util.Collections;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  public OpenAPI customOpenAPI(){
    return new OpenAPI().servers(Collections.singletonList(
        new Server().url("http://localhost:8080").description("local server")
    )).info(new Info().title("Owl Blog Backend API").version("1.0.0"));
  }
}
