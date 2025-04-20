package me.blog.backend.bounded.context.history.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.port.in.historyUseCase;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
  public void saveIPInformation(String ipAddress) {
    String url = API_URL + ipAddress;
    GeoIpResponse result = restTemplate.getForObject(url, GeoIpResponse.class);
    GeoLocationEntity geoLocationEntity = GeoLocationEntity.builder()
        .query(result.query)
        .status(result.status)
        .continent(result.continent)
        .continentCode(result.continentCode)
        .country(result.country)
        .countryCode(result.countryCode)
        .region(result.region)
        .regionName(result.regionName)
        .city(result.city)
        .district(result.district)
        .zip(result.zip)
        .timezone(result.timezone)
        .isp(result.isp)
        .mobile(result.mobile)
        .proxy(result.proxy)
        .hosting(result.hosting)
        .lat(result.lat)
        .lon(result.lon)
        .createdAt(LocalDateTime.now())
        .build();

    geoLocationRepository.save(geoLocationEntity);
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


  public record GeoIpResponse(String query, String status, String continent, String continentCode, String country, String countryCode, String region,
                              String regionName, String city, String district, String zip, String timezone, String isp, String mobile, String proxy,
                              String hosting, String lat, String lon) {}
}
