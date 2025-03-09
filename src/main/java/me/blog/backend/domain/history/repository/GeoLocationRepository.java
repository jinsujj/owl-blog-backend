package me.blog.backend.domain.history.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import me.blog.backend.domain.history.entity.GeoLocationEntity;

public interface GeoLocationRepository extends JpaRepository<GeoLocationEntity, Long> {
  long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
