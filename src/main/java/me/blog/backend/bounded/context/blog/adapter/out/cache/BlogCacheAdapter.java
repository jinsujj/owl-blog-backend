package me.blog.backend.bounded.context.blog.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogRepositoryAdapter;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogCachePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogCacheAdapter extends AbstractCache<BlogEntity> implements BlogCachePort {
    private final BlogRepositoryAdapter blogRepository;
    private volatile List<BlogEntity> immutableList = List.of();
    private volatile boolean isCached = false;

    @Override
    public List<BlogEntity> findAll() {
        return immutableList;
    }

    @Override
    public List<BlogEntity> findByAuthor(String author) {
        return immutableList.stream()
                .filter(b -> b.getAuthor().equals(author))
                .toList();
    }

    @Override
    public Optional<BlogEntity> findById(Long id) {
        return immutableList.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<BlogEntity> findByType(String type) {
        return immutableList.stream()
                .filter(b -> type != null && type.equals(b.getType()))
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public void putAll() {
        List<BlogEntity> blogList = blogRepository.findAllWithRelationsForCache();
        immutableList = List.copyOf(blogList);
        isCached = true;
    }

    @Override
    public boolean isCached() {
        return isCached;
    }

    @Override
    public void evictAll() {
        immutableList = List.of();
        isCached = false;
    }
}
