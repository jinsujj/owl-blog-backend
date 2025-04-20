package me.blog.backend.bounded.context.history.port.out;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;

import java.time.LocalDateTime;

public interface GeoLocationRepositoryPort {
    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    GeoLocationEntity save(GeoLocationEntity entity);
    long count();
}
