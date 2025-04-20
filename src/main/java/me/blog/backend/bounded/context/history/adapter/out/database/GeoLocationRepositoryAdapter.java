package me.blog.backend.bounded.context.history.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class GeoLocationRepositoryAdapter implements GeoLocationRepositoryPort {
    private final GeoLocationRepository geoLocationRepository;

    @Override
    public long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return geoLocationRepository.countByCreatedAtBetween(from, to);
    }

    @Override
    public GeoLocationEntity save(GeoLocationEntity entity) {
        return geoLocationRepository.save(entity);
    }

    @Override
    public long count() {
        return geoLocationRepository.count();
    }
}
