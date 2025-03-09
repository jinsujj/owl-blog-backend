package me.blog.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.domain.history.service.GeolocationService;

@RestController
@RequestMapping("visitor")
public class VisitorController {
  private final GeolocationService geolocationService;

  public VisitorController(GeolocationService geolocationService) {
    this.geolocationService = geolocationService;
  }

  @GetMapping("/todayCnt")
  public ResponseEntity<Long> getTodayCnt(){
    return ResponseEntity.ok(geolocationService.getTodayCount());
  }

  @GetMapping("/totalCnt")
  public ResponseEntity<Long> getTotalCnt(){
    return ResponseEntity.ok(geolocationService.getTotalCount());
  }
}
