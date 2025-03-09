package me.blog.backend.api;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.domain.blog.service.BlogService;
import me.blog.backend.domain.blog.service.SeriesService;
import me.blog.backend.domain.blog.vo.BlogVO;
import me.blog.backend.domain.blog.vo.SeriesVO;

@RestController
@RequestMapping("/series")
public class SeriesController {
  private final BlogService blogService;
  private final SeriesService seriesService;

  public SeriesController(BlogService blogService, SeriesService seriesService) {
    this.blogService = blogService;
    this.seriesService = seriesService;
  }

  @GetMapping("/")
  public ResponseEntity<List<SeriesVO>> getSeries() {
    return ResponseEntity.ok(seriesService.getSeries());
  }

  @GetMapping("/blogs")
  public ResponseEntity<Map<String, List<BlogVO>>> getBlogsBySeries() {
    return ResponseEntity.ok(blogService.getBlogGroupBySeries());
  }

  @PostMapping("/{seriesName}")
  public ResponseEntity<Boolean> createSeries(@PathVariable String seriesName) {
    return ResponseEntity.ok(seriesService.createSeries(seriesName));
  }

  @DeleteMapping("/")
  public ResponseEntity<Boolean> deleteSeries(@PathVariable String seriesName) {
    return ResponseEntity.ok(seriesService.removeSeries(seriesName));
  }

  @PostMapping("/{seriesName}/blog/{blogId}")
  public ResponseEntity<Boolean> addBlogToSeries(@PathVariable String seriesName, @PathVariable String blogId) {
    return ResponseEntity.ok(seriesService.addBlogToSeries(blogId, seriesName));
  }

}
