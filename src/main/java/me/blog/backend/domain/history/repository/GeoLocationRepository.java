package me.blog.backend.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import me.blog.backend.domain.history.entity.GeoLocationEntity;

public interface GeoLocationRepository extends JpaRepository<GeoLocationEntity, Long> {
}
