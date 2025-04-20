package me.blog.backend.bounded.context.blog.port.out.repository;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;

import java.util.List;
import java.util.Optional;

public interface BlogRepositoryPort {
    List<BlogEntity> findAll();
    Optional<BlogEntity> findById(Long id);
    Optional<BlogEntity> findByType(String type);
    List<BlogEntity> findByAuthor(String author);
    List<BlogEntity> findAllWithRelationsForCache();

    BlogEntity save(BlogEntity blog);
    void incrementReadCount(Long id);
}
