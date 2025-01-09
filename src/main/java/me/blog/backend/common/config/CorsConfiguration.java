package me.blog.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost", "https://localhost")
        .allowedMethods("GET", "POST", "PUT", "OPTIONS")
        .allowedHeaders("Authorization", "Content-Type")
        .maxAge(3600);
  }

}
