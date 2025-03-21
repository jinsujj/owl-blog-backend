package me.blog.backend.domain.history.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name ="geo_location")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoLocationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String query;
  private String status;
  private String continent;
  private String continentCode;
  private String country;
  private String countryCode;
  private String region;
  private String regionName;
  private String city;
  private String district;
  private String zip;
  private String timezone;
  private String isp;
  private String mobile;
  private String proxy;
  private String hosting;
  private String lat;
  private String lon;
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public GeoLocationEntity(String query, String status, String continent, String continentCode, String country, String countryCode,
      String region, String regionName, String city, String district, String zip, String timezone, String isp,
      String mobile, String proxy, String hosting, String lat, String lon) {
    this.query = query;
    this.status = status;
    this.continent = continent;
    this.continentCode = continentCode;
    this.country = country;
    this.countryCode = countryCode;
    this.region = region;
    this.regionName = regionName;
    this.city = city;
    this.district = district;
    this.zip = zip;
    this.timezone = timezone;
    this.isp = isp;
    this.mobile = mobile;
    this.proxy = proxy;
    this.hosting = hosting;
    this.lat = lat;
    this.lon = lon;
    createdAt = LocalDateTime.now();
  }
}
