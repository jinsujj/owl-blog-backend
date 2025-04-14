package me.blog.backend.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import me.blog.backend.domain.blog.cache.AbstractCacheManager;
import me.blog.backend.domain.blog.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import me.blog.backend.domain.blog.service.BlogService;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.vo.TagVO;
import me.blog.backend.domain.blog.service.TagService;
import me.blog.backend.domain.history.service.GeolocationService;

@RestController
@RequestMapping("/blogs")
public class BlogController {
  private final BlogService blogService;
  private final TagService tagService;
  private final KakaoAuthService authService;
  private final HttpServletRequest request;
  private final GeolocationService geolocationService;
  private final AbstractCacheManager cacheManager;

  public BlogController(BlogService blogService, TagService tagService, KakaoAuthService authService, HttpServletRequest request, GeolocationService geolocationService, AbstractCacheManager cacheManager) {
    this.blogService = blogService;
    this.tagService = tagService;
    this.authService = authService;
    this.request = request;
    this.geolocationService = geolocationService;
    this.cacheManager = cacheManager;
  }

  @PostMapping
  public ResponseEntity<BlogVO> postBlog(@RequestBody BlogRequest blogRequest) {
    BlogVO blog = blogService.postBlog(blogRequest.userId, blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl, blogRequest.type);

    if(blogRequest.tags != null)
      tagService.postTags(blogRequest.tags, blog.id());

    return ResponseEntity.ok(blog);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogVO> updateBlog(@PathVariable Long id, @RequestBody BlogRequest blogRequest) {
    BlogVO updatedBlog = blogService.updateBlog(id, blogRequest.userId, blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl);

    if(blogRequest.tags != null)
      tagService.updateTags(blogRequest.tags, id);

    return ResponseEntity.ok(updatedBlog);
  }

  @PutMapping("/content/{id}")
  public ResponseEntity<BlogVO> updateContentBlog(@PathVariable Long id, @RequestBody String content) {
    BlogVO updatedBlog = blogService.updateBlogContent(id, content);

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
  public ResponseEntity<List<BlogSummary>> getAllBlogs(@CookieValue(value ="token", required = false) String token) {
    if (token != null){
      String userId = String.valueOf(authService.getUserByToken(token).id());
      return ResponseEntity.ok(blogService.getAllBlogsByUser(userId).stream()
            .map(blog -> new BlogSummary(
                    blog.id(),
                    blog.title(),
                    blog.summary(),
                    blog.thumbnailUrl(),
                    blog.readCount(),
                    blog.updatedAt(),
                    blog.publishedAt(),
                    blog.tags()
            )).toList());
    }
    return ResponseEntity.ok(blogService.getAllBlogs().stream()
            .map(blog -> new BlogSummary(
                    blog.id(),
                    blog.title(),
                    blog.summary(),
                    blog.thumbnailUrl(),
                    blog.readCount(),
                    blog.updatedAt(),
                    blog.publishedAt(),
                    blog.tags()
            )).toList());
  }

  @GetMapping("/series")
  public ResponseEntity<Map<String, List<BlogVO>>> getBlogsBySeries() {
    return ResponseEntity.ok(blogService.getBlogGroupBySeries());
  }



  @GetMapping("/{id}")
  public ResponseEntity<BlogVO> getBlogById(@PathVariable Long id) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress != null && !ipAddress.isEmpty()) {
      String clientIp = ipAddress.split(",")[0].trim();
      geolocationService.saveIPInformation(clientIp);
    }

    return ResponseEntity.ok(blogService.getBlogById(id));
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<BlogVO> getBlogsByType(@PathVariable String type) {
    return ResponseEntity.ok(blogService.getBlogByType(type));
  }

  @GetMapping("/tags")
  public ResponseEntity<TagVO[]> getTagsAll(){
    return ResponseEntity.ok(tagService.getTagsAll());
  }

  @GetMapping("/{id}/tags")
  public ResponseEntity<TagVO[]> getTagsById(@PathVariable Long id){
    return ResponseEntity.ok(tagService.getTagByBlogId(id));
  }

  @PostMapping("/caches/refresh")
  public ResponseEntity<Void> refreshCache(){
    cacheManager.refreshAllCaches();
    return ResponseEntity.noContent().build();
  }

  public record BlogRequest(String userId, String title, String content, String thumbnailUrl, List<TagVO> tags, String type) {}

  public record BlogSummary(Long id, String title, String summary, String thumbnailUrl, int readCount, LocalDateTime updatedAt, LocalDateTime publishedAt, TagVO[] tags) {}
}
