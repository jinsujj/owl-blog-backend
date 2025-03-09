package me.blog.backend.domain.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.domain.blog.entity.SeriesEntity;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {
  Optional<SeriesEntity> findByName(String name);
}
