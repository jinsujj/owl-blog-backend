package me.blog.backend.bounded.context.blog.adapter.out.database;

import java.util.Optional;

import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {
  Optional<SeriesEntity> findByName(String name);
}
