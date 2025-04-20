package me.blog.backend.bounded.context.blog.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.port.out.repository.SeriesRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryAdapter implements SeriesRepositoryPort {
    private final SeriesRepository repository;

    @Override
    public Optional<SeriesEntity> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public SeriesEntity save(SeriesEntity entity) {
        return repository.save(entity);
    }

    @Override
    public List<SeriesEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(SeriesEntity entity) {
        repository.delete(entity);
    }
}
