package me.blog.backend.bounded.context.blog.adapter.in.api;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.auth.application.service.KakaoAuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import me.blog.backend.bounded.context.blog.adapter.in.batch.CacheManagerAdapter;
import me.blog.backend.bounded.context.blog.application.service.SeriesService;
import me.blog.backend.bounded.context.blog.domain.vo.SeriesVO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/series")
public class SeriesController {
  private final SeriesService seriesService;
  private final KakaoAuthService authService;
  private final CacheManagerAdapter cacheManager;

  @GetMapping("/")
  public ResponseEntity<List<SeriesVO>> getSeries() {
    return ResponseEntity.ok(seriesService.getSeries());
  }

  @PostMapping("/{seriesName}")
  public ResponseEntity<Boolean> createSeries(
          @Parameter(hidden = true)
          @CookieValue(value ="token", required = false) String token,
          @PathVariable String seriesName) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }
    cacheManager.refreshAllCaches();
    return ResponseEntity.ok(seriesService.createSeries(seriesName));
  }

  @DeleteMapping("/{seriesName}")
  public ResponseEntity<Boolean> deleteSeries(
          @Parameter(hidden = true)
          @CookieValue(value ="token", required = false) String token,
          @PathVariable String seriesName) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }
    cacheManager.refreshAllCaches();
    return ResponseEntity.ok(seriesService.removeSeries(seriesName));
  }

  @PostMapping("/{seriesName}/blog/{blogId}")
  public ResponseEntity<Boolean> addBlogToSeries(
          @CookieValue(value ="token", required = false) String token,
          @PathVariable String seriesName, @PathVariable String blogId) {

    if(token == null || !authService.validateToken(token)){
      return ResponseEntity.status(401).build();
    }
    cacheManager.refreshAllCaches();
    return ResponseEntity.ok(seriesService.addBlogToSeries(blogId, seriesName));
  }

}
