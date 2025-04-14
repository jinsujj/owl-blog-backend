package me.blog.backend.domain.blog.cache;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.repository.BlogRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class BlogCache extends AbstractCache<BlogEntity> {
    private final BlogRepository blogRepository;

    public BlogCache(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Optional<BlogEntity> findById(Long id) {
        return immutableList.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public Optional<BlogEntity> findByType(String type) {
        return immutableList.stream()
                .filter(b -> b.getType() != null && b.getType().equals(type))
                .findFirst();
    }

    public List<BlogEntity> findByAuthor(String author) {
        return immutableList.stream()
                .filter(b -> b.getAuthor().equals(author))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void putAll() {
        List<BlogEntity> blogList = blogRepository.findAllWithRelationsForCache();
        immutableList = List.copyOf(blogList);
        isCached = true;
    }
}
