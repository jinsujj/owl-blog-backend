package me.blog.backend.bounded.context.history.adapter.out.database;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.QBlogEntity;
import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.model.QGeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GeoLocationRepositoryAdapter implements GeoLocationRepositoryPort {
    private final GeoLocationRepository geoLocationRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return geoLocationRepository.countByCreatedAtBetween(from, to);
    }

    @Override
    public List<GeoLocationEntity> getByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return geoLocationRepository.findByCreatedAtBetween(from, to);
    }

    @Override
    public List<CoordinateWithBlogVO> getCoordinatesHistoryWithAIpAddress(LocalDate from, LocalDate to, String ipAddress) {
        QGeoLocationEntity geoLocation = QGeoLocationEntity.geoLocationEntity;
        QBlogEntity blog = QBlogEntity.blogEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(geoLocation.createdAt.between(from.atStartOfDay(), to.plusDays(1).atStartOfDay()));

        // ipAddress is Optional
        if (ipAddress != null && !ipAddress.isBlank())
            builder.and(geoLocation.query.eq(ipAddress));

        return queryFactory.select(
                Projections.constructor(
                        CoordinateWithBlogVO.class,
                        blog.title,
                        blog.id,
                        geoLocation.country,
                        geoLocation.city,
                        geoLocation.query,
                        geoLocation.createdAt,
                        geoLocation.lat,
                        geoLocation.lon
                ))
                .from(geoLocation)
                .leftJoin(geoLocation.blog, blog)
                .where(builder)
                .orderBy(geoLocation.id.desc())
                .fetch();
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
