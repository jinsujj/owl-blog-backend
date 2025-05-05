package me.blog.backend.bounded.context.history.port.out;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GeoLocationRepositoryPort {
    List<GeoLocationEntity> getByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<CoordinateWithBlogVO> getCoordinatesHistoryWithAIpAddress(LocalDate from, LocalDate to, String ipAddress);
    GeoLocationEntity save(GeoLocationEntity entity);
    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    long count();
}
