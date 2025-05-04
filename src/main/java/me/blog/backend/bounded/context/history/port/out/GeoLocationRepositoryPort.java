package me.blog.backend.bounded.context.history.port.out;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface GeoLocationRepositoryPort {
    List<GeoLocationEntity> getByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    GeoLocationEntity save(GeoLocationEntity entity);
    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    long count();
}
