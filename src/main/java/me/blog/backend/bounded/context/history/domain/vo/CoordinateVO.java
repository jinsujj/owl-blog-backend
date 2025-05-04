package me.blog.backend.bounded.context.history.domain.vo;

import me.blog.backend.bounded.context.history.domain.model.GeoLocationEntity;

import java.time.LocalDateTime;

public record CoordinateVO(
        String country,
        String city,
        String ip,
        LocalDateTime createdAt,
        String lat,
        String lon
){
    public static CoordinateVO fromEntity(GeoLocationEntity entity){
        return new CoordinateVO(
                entity.getCountry(),
                entity.getCity(),
                entity.getQuery(),
                entity.getCreatedAt(),
                entity.getLat(),
                entity.getLon()
        );
    }
}


