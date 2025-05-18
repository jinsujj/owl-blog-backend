package me.blog.backend.common.config;

import java.util.Arrays;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
        Parameter cookieToken = new Parameter()
                .in("cookie")
                .name("token")
                .required(false)
                .description("JWT Token in Cookie");

        return new OpenAPI()
            .components(new Components()
                .addParameters("token", cookieToken)
                .addSecuritySchemes("cookieAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.COOKIE)
                    .name("token")
                ))
            .addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
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