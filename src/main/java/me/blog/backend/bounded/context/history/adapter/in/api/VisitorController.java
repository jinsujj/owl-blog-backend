package me.blog.backend.bounded.context.history.adapter.in.api;

import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.blog.backend.bounded.context.history.application.service.GeolocationService;

import java.time.LocalDate;
import java.util.List;

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

  @GetMapping("/coordinate/history")
  public ResponseEntity<List<CoordinateWithBlogVO>> getIpHistory(
          @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
          @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
          @RequestParam(value ="ip", required = false) String ipAddress){

    List<CoordinateWithBlogVO> result = geolocationService.getCoordinatesHistoryWithAIpAddress(from, to, ipAddress);
    return ResponseEntity.ok(result);
  }
}
