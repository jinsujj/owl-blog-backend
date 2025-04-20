package me.blog.backend.bounded.context.blog.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.port.out.BlogRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogRepositoryAdapter implements BlogRepositoryPort {
    private final BlogRepository blogRepository;

    @Override
    public List<BlogEntity> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Optional<BlogEntity> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Optional<BlogEntity> findByType(String type) {
        return blogRepository.findByType(type);
    }

    @Override
    public List<BlogEntity> findByAuthor(String author) {
        return blogRepository.findByAuthor(author);
    }

    @Override
    public List<BlogEntity> findAllWithRelationsForCache() {
        return blogRepository.findAllWithRelationsForCache();
    }

    @Override
    public BlogEntity save(BlogEntity blog) {
        return blogRepository.save(blog);
    }

    @Override
    public void incrementReadCount(Long id) {
        blogRepository.incrementReadCount(id);
    }
}
