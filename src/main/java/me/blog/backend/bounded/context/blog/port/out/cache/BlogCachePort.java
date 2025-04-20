package me.blog.backend.bounded.context.blog.port.out.cache;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;

import java.util.List;
import java.util.Optional;

public interface BlogCachePort {
    List<BlogEntity> findAll();
    List<BlogEntity> findByAuthor(String author);
    Optional<BlogEntity> findById(Long id);
    Optional<BlogEntity> findByType(String type);
    void putAll();
    boolean isCached();
    void evictAll();
}
