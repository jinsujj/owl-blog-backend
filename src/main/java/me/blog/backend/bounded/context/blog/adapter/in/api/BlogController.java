package me.blog.backend.bounded.context.blog.adapter.in.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.in.batch.CacheManagerAdapter;
import me.blog.backend.bounded.context.auth.application.service.KakaoAuthService;
import me.blog.backend.bounded.context.blog.adapter.out.message.BlogVisitorPublisherAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import me.blog.backend.bounded.context.blog.application.service.BlogService;
import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;
import me.blog.backend.bounded.context.blog.domain.vo.TagVO;
import me.blog.backend.bounded.context.blog.application.service.TagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blogs")
public class BlogController {
  private final BlogService blogService;
  private final TagService tagService;
  private final KakaoAuthService authService;
  private final HttpServletRequest request;
  private final CacheManagerAdapter cacheManager;
  private final BlogVisitorPublisherAdapter blogVisitorPublisherAdapter;

  @PostMapping
  public ResponseEntity<BlogVO> postBlog(@Parameter(hidden = true) @CookieValue(value ="token", required = false) String token,
                                         @RequestBody BlogRequest blogRequest) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }

    BlogVO blog = blogService.postBlog(blogRequest.userId, blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl, blogRequest.type);

    if(blogRequest.tags != null){
      tagService.postTags(blogRequest.tags, blog.id());
      cacheManager.refreshAllCaches();
    }

    return ResponseEntity.ok(blog);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BlogVO> updateBlog(@Parameter(hidden = true) @CookieValue(value ="token", required = false) String token,
                                           @PathVariable Long id, @RequestBody BlogRequest blogRequest) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }

    BlogVO updatedBlog = blogService.updateBlog(id, blogRequest.userId, blogRequest.title, blogRequest.content, blogRequest.thumbnailUrl);

    if(blogRequest.tags != null){
      tagService.updateTags(blogRequest.tags, id);
      cacheManager.refreshAllCaches();
    }

    return ResponseEntity.ok(updatedBlog);
  }

  @PutMapping("/content/{id}")
  public ResponseEntity<BlogVO> updateContentBlog(@Parameter(hidden = true) @CookieValue(value ="token", required = false) String token,
                                                  @PathVariable Long id, @RequestBody String content) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }

    BlogVO updatedBlog = blogService.updateBlogContent(id, content);

    return ResponseEntity.ok(updatedBlog);
  }

  @PostMapping("/{id}/publish")
  public ResponseEntity<BlogVO> publishBlog(@Parameter(hidden = true) @CookieValue(value ="token", required = false) String token,
                                            @PathVariable Long id) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }

    BlogVO blog = blogService.publishBlog(id);
    return ResponseEntity.ok(blog);
  }

  @PostMapping("/{id}/unpublish")
  public ResponseEntity<BlogVO> unpublishBlog(@Parameter(hidden = true) @CookieValue(value ="token", required = false) String token,
                                              @PathVariable Long id) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }

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
    saveVisitorIpHistory(String.valueOf(id));
    var blog = blogService.getBlogById(id);
    return ResponseEntity.of(blog);
  }

  private void saveVisitorIpHistory(String blogId) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    String remoteAddr = request.getRemoteAddr();
    String clientIp = (ipAddress != null && !ipAddress.isEmpty())
            ? ipAddress.split(",")[0].trim()
            : remoteAddr;

    System.out.printf("[Visitor IP] X-Forwarded-For: %s | RemoteAddr: %s | FinalClientIp: %s%n", ipAddress, remoteAddr, clientIp);

    try {
        blogVisitorPublisherAdapter.publish(clientIp, blogId);
    } catch (Exception e) {
        System.err.printf("[Visitor IP] 기록 실패: %s%n", e.getMessage());
    }
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
