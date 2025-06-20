package me.blog.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import me.blog.backend.bounded.context.blog.domain.vo.BlogSeriesVO;
import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;

@Configuration
public class RedisConfig {
  @Bean
  public RedisTemplate<String, BlogVO> blogVoRedisTemplate(RedisConnectionFactory factory) {
    return createTypedRedisTemplate(factory, BlogVO.class);
  }

  @Bean
  public RedisTemplate<String, BlogSeriesVO> blogSeriesRedisTemplate(RedisConnectionFactory factory) {
    return createTypedRedisTemplate(factory, BlogSeriesVO.class);
  }

  private <T> RedisTemplate<String, T> createTypedRedisTemplate(RedisConnectionFactory factory, Class<T> clazz) {
    RedisTemplate<String, T> template = new RedisTemplate<>();

    Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper(), clazz);

    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setDefaultSerializer(serializer);
    template.afterPropertiesSet();

    return template;
  }


  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .registerModule(new Hibernate5Module().disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION))
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

}
