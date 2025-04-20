package me.blog.backend.bounded.context.auth.config;

import me.blog.backend.bounded.context.auth.domain.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expirationTime) {
        return new JwtTokenProvider(secretKey, expirationTime);
    }
}
