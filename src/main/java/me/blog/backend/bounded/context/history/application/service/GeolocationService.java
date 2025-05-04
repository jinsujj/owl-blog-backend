package me.blog.backend.bounded.context.history.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.vo.CoordinateVO;
import me.blog.backend.bounded.context.history.domain.vo.GeoIpResponseVO;
import me.blog.backend.bounded.context.history.port.in.historyUseCase;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static me.blog.backend.bounded.context.history.domain.vo.CoordinateVO.fromEntity;

@Service
public class GeolocationService implements historyUseCase {
  private final GeoLocationRepositoryPort geoLocationRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final static String API_URL = "http://ip-api.com/json/";

  public GeolocationService(GeoLocationRepositoryPort geoLocationRepository) {
    this.geoLocationRepository = geoLocationRepository;
  }

  @Override
  @Transactional
  public void saveIPInformation(String ipAddress, LocalDateTime createdTime) {
    String url = API_URL + ipAddress;
    GeoIpResponseVO result = restTemplate.getForObject(url, GeoIpResponseVO.class);
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
        .build();

    geoLocationRepository.save(geoLocationEntity);
  }

  @Override
  public List<CoordinateVO> getCoordinatesHistory(LocalDate from, LocalDate to) {
    List<GeoLocationEntity> geoLocationEntityList = geoLocationRepository.getByCreatedAtBetween(from.atStartOfDay(), to.atTime(LocalTime.MAX));
    List<CoordinateVO> result = new ArrayList<>();

    for(GeoLocationEntity geoLocationEntity : geoLocationEntityList) {
      if (geoLocationEntity.getCountry() == null) continue;

      result.add(fromEntity(geoLocationEntity));
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
