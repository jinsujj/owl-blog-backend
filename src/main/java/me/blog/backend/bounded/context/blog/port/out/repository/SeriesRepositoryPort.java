package me.blog.backend.bounded.context.blog.port.out.repository;

import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;

import java.util.List;
import java.util.Optional;

public interface SeriesRepositoryPort {
    Optional<SeriesEntity> findByName(String name);
    SeriesEntity save(SeriesEntity entity);
    List<SeriesEntity> findAll();
    void delete(SeriesEntity entity);
}
