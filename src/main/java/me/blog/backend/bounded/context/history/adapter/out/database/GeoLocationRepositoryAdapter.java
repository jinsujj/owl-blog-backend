package me.blog.backend.bounded.context.history.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogRepositoryPort;
import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;
import me.blog.backend.bounded.context.history.domain.vo.CoordinateWithBlogVO;
import me.blog.backend.bounded.context.history.port.out.GeoLocationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GeoLocationRepositoryAdapter implements GeoLocationRepositoryPort {
    private final GeoLocationRepository geoLocationRepository;
    private final BlogRepositoryPort blogRepository;

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
        List<GeoLocationEntity> geoLocationEntityList;
        if (ipAddress == null || ipAddress.isEmpty()) {
            geoLocationEntityList = geoLocationRepository.findByCreatedAtBetweenOrderByQueryAsc(
                    from.atStartOfDay(), to.plusDays(1).atStartOfDay());
        }
        else {
            geoLocationEntityList = geoLocationRepository.findByCreatedAtBetweenAndQueryOrderByQueryAsc(
                    from.atStartOfDay(), to.plusDays(1).atStartOfDay(), ipAddress);
        }
        return transferToDTO(geoLocationEntityList);
    }

    private List<CoordinateWithBlogVO> transferToDTO(List<GeoLocationEntity> geoLocationEntityList) {
        List<CoordinateWithBlogVO> result = new ArrayList<>();
        for (GeoLocationEntity geoLocation : geoLocationEntityList) {
            Long blogId = geoLocation.getBlog_id();
            if (blogId == null) continue;

            Optional<BlogEntity> blogEntity = blogRepository.findById(blogId);
            if(blogEntity.isPresent()){
                BlogEntity blog = blogEntity.get();
                result.add(new CoordinateWithBlogVO(
                    blog.getTitle(),
                    blog.getId(),
                    geoLocation.getCountry(),
                    geoLocation.getCity(),
                    geoLocation.getQuery(),
                    geoLocation.getCreatedAt(),
                    geoLocation.getLat(),
                    geoLocation.getLon()
                ));
            }
            else {
                result.add(new CoordinateWithBlogVO(
                    "",
                    geoLocation.getBlog_id(),
                    geoLocation.getCountry(),
                    geoLocation.getCity(),
                    geoLocation.getQuery(),
                    geoLocation.getCreatedAt(),
                    geoLocation.getLat(),
                    geoLocation.getLon()
                ));
            }
        }
        return result;
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
