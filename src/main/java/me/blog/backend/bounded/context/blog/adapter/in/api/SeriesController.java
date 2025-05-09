package me.blog.backend.bounded.context.blog.adapter.in.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.bounded.context.blog.application.service.SeriesService;
import me.blog.backend.bounded.context.blog.domain.vo.SeriesVO;

@RestController
@RequestMapping("/series")
public class SeriesController {
  private final SeriesService seriesService;

  public SeriesController(SeriesService seriesService) {
    this.seriesService = seriesService;
  }

  @GetMapping("/")
  public ResponseEntity<List<SeriesVO>> getSeries() {
    return ResponseEntity.ok(seriesService.getSeries());
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
