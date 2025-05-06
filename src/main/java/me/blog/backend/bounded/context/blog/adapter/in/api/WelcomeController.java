package me.blog.backend.bounded.context.blog.adapter.in.api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("!prod")
public class WelcomeController {

  @GetMapping
  public String SwaggerPage(){
    return "redirect:/swagger-ui/index.html";
  }
}
