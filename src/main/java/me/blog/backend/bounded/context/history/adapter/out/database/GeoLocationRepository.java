package me.blog.backend.bounded.context.history.adapter.out.database;

import java.time.LocalDateTime;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocationRepository extends JpaRepository<GeoLocationEntity, Long> {
  long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
