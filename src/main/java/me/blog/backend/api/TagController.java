package me.blog.backend.api;

import org.springframework.stereotype.Controller;

import me.blog.backend.domain.blog.service.BlogService;
import me.blog.backend.domain.blog.service.TagService;

@Controller
public class TagController {
  private final BlogService blogService;
  private final TagService tagService;

  public TagController(BlogService blogService, TagService tagService) {
    this.blogService = blogService;
    this.tagService = tagService;
  }


}
