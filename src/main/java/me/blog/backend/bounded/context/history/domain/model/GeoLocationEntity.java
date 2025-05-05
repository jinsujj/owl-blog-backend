package me.blog.backend.bounded.context.history.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;

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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="blog_id")
  private BlogEntity blog;
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

}
