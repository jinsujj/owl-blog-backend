package me.blog.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OwlBlogBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(OwlBlogBackendApplication.class, args);
  }

}
