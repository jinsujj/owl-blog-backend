package me.blog.backend.api;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.domain.blog.service.BlogService;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.vo.TagVO;
import me.blog.backend.domain.blog.service.TagService;

@RestController
@RequestMapping("/blogs")
public class BlogController {
  private final BlogService blogService;
  private final TagService tagService;

  public BlogController(BlogService blogService, TagService tagService) {
    this.blogService = blogService;
    this.tagService = tagService;
  }

  @PostMapping
  public ResponseEntity<BlogVO> postBlog(@RequestBody BlogRequest blogRequest) {
    BlogVO blog = blogService.postBlog(blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl);

    if(blogRequest.tags != null)
      tagService.postTags(blogRequest.tags, blog.id());

    return ResponseEntity.ok(blog);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogVO> updateBlog(@PathVariable Long id, @RequestBody BlogRequest blogRequest) {
    BlogVO updatedBlog = blogService.updateBlog(id, blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl);

    if(blogRequest.tags != null)
      tagService.updateTags(blogRequest.tags, id);

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

  @GetMapping("/summary")
  public ResponseEntity<List<BlogSummary>> getAllBlogs() {
    List<BlogSummary> simplifiedBlogs = blogService.getAllBlogs().stream()
        .map(blog -> new BlogSummary(
            blog.id(),
            blog.title(),
            blog.summary(),
            blog.thumbnailUrl(),
            blog.readCount(),
            blog.updatedAt(),
            blog.publishedAt(),
            blog.tags()
        )).toList();

    return ResponseEntity.ok(simplifiedBlogs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BlogVO> getBlogById(@PathVariable Long id) {
    return ResponseEntity.ok(blogService.getBlogById(id));
  }

  @GetMapping("/tags")
  public ResponseEntity<TagVO[]> getTagsAll(){
    return ResponseEntity.ok(tagService.getTagsAll());
  }

  @GetMapping("/{id}/tags")
  public ResponseEntity<TagVO[]> getTagsById(@PathVariable Long id){
    return ResponseEntity.ok(tagService.getTagByBlogId(id));
  }

  public record BlogRequest(String title, String content, String thumbnailUrl, List<TagVO> tags) {}

  public record BlogSummary(Long id, String title, String summary, String thumbnailUrl, int readCount, LocalDateTime updatedAt, LocalDateTime publishedAt, TagVO[] tags) {}
}
