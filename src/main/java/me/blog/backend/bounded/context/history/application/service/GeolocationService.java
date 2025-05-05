package me.blog.backend.bounded.context.history.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;
import me.blog.backend.bounded.context.history.domain.vo.GeoIpResponseVO;
import me.blog.backend.bounded.context.history.port.in.historyUseCase;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeolocationService implements historyUseCase {
  private final GeoLocationRepositoryPort geoLocationRepository;
  private final BlogRepositoryPort blogRepositoryPort;
  private final RestTemplate restTemplate = new RestTemplate();
  private final static String API_URL = "http://ip-api.com/json/";


  @Override
  @Transactional
  public void saveIPInformation(String ipAddress, String blogId, LocalDateTime createdTime) {
    String url = API_URL + ipAddress;
    GeoIpResponseVO result = restTemplate.getForObject(url, GeoIpResponseVO.class);
    BlogEntity blog = blogRepositoryPort.findById(Long.parseLong(blogId)).orElse(null);

    GeoLocationEntity geoLocationEntity = GeoLocationEntity.builder()
        .query(result.query())
        .status(result.status())
        .continent(result.continent())
        .continentCode(result.continentCode())
        .country(result.country())
        .countryCode(result.countryCode())
        .region(result.region())
        .regionName(result.regionName())
        .city(result.city())
        .district(result.district())
        .zip(result.zip())
        .timezone(result.timezone())
        .isp(result.isp())
        .mobile(result.mobile())
        .proxy(result.proxy())
        .hosting(result.hosting())
        .lat(result.lat())
        .lon(result.lon())
        .createdAt(createdTime)
        .blog(blog)
        .build();

    geoLocationRepository.save(geoLocationEntity);
  }

  @Override
  public List<CoordinateWithBlogVO> getCoordinatesHistoryWithAIpAddress(LocalDate from, LocalDate to, String ipAddress) {
    List<CoordinateWithBlogVO> historyWithIpList= geoLocationRepository.getCoordinatesHistoryWithAIpAddress(from, to, ipAddress);
    List <CoordinateWithBlogVO> result = new ArrayList<>();

    for(CoordinateWithBlogVO coordinateWithBlogVO : historyWithIpList) {
      if(coordinateWithBlogVO.country() == null) continue;

      result.add(coordinateWithBlogVO);
    }

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public long getTodayCount(){
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(23, 59, 59);
    return geoLocationRepository.countByCreatedAtBetween(startOfDay, endOfDay);
  }

  @Override
  @Transactional(readOnly = true)
  public long getTotalCount(){
    return geoLocationRepository.count();
  }
}
