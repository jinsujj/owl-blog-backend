package me.blog.backend.bounded.context.history.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "geo_location", uniqueConstraints = {
    @UniqueConstraint(name = "uq_query_created_blog", columnNames = { "query", "created_at", "blog_id" })
})
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoLocationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long blog_id;
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
