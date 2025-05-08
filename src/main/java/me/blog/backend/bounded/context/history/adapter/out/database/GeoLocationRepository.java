package me.blog.backend.bounded.context.history.adapter.out.database;

import java.time.LocalDateTime;
import java.util.List;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocationRepository extends JpaRepository<GeoLocationEntity, Long> {
  long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
  List<GeoLocationEntity> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
  List<GeoLocationEntity> findByCreatedAtBetweenOrderByQueryAsc(LocalDateTime from, LocalDateTime to);
  List<GeoLocationEntity> findByCreatedAtBetweenAndQueryOrderByQueryAsc(LocalDateTime from, LocalDateTime to, String query);
}
