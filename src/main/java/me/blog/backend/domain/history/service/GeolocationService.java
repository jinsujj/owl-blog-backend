package me.blog.backend.domain.history.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import me.blog.backend.domain.history.entity.GeoLocationEntity;
import me.blog.backend.domain.history.repository.GeoLocationRepository;

@Service
public class GeolocationService {
  private final GeoLocationRepository geoLocationRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final static String API_URL = "http://ip-api.com/json/";

  public GeolocationService(GeoLocationRepository geoLocationRepository) {
    this.geoLocationRepository = geoLocationRepository;
  }

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

  public record GeoIpResponse(String query, String status, String continent, String continentCode, String country, String countryCode, String region,
                              String regionName, String city, String district, String zip, String timezone, String isp, String mobile, String proxy,
                              String hosting, String lat, String lon) {}
}
