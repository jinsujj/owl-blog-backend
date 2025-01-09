package me.blog.backend.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.domain.blog.BlogService;
import me.blog.backend.domain.blog.BlogVO;


@RestController
@RequestMapping("/blogs")
public class BlogController {
  private final BlogService blogService;

  public BlogController(BlogService blogService) {
    this.blogService = blogService;
  }

  @PostMapping
  public ResponseEntity<BlogVO> createBlog(@RequestBody BlogVO blogRequest) {
    BlogVO blog = blogService.createBlog(blogRequest.title(), blogRequest.content(), blogRequest.attachments());
    return ResponseEntity.ok(blog);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogVO> updateBlog(@PathVariable Long id, @RequestBody BlogVO blogRequest) {
    BlogVO updatedBlog = blogService.updateBlog(id, blogRequest.title(), blogRequest.content(), blogRequest.attachments());
    return ResponseEntity.ok(updatedBlog);
  }

  @PostMapping("/{id}/publish")
  public ResponseEntity<BlogVO> publishBlog(@PathVariable Long id) {
    BlogVO blog = blogService.publishBlog(id);
    return ResponseEntity.ok(blog);
  }

  @PostMapping("/{id}/unpublish")
  public ResponseEntity<BlogVO> unpublishBlog(@PathVariable Long id) {
    BlogVO blog = blogService.unPublishBlog(id);
    return ResponseEntity.ok(blog);
  }

  @GetMapping
  public ResponseEntity<List<BlogVO>> getAllBlogs() {
    return ResponseEntity.ok(blogService.getAllBlogs());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BlogVO> getBlogById(@PathVariable Long id) {
    return ResponseEntity.ok(blogService.getBlogById(id));
  }
}
