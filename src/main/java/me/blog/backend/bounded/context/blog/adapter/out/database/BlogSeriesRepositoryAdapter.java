package me.blog.backend.bounded.context.blog.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.port.out.BlogSeriesRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogSeriesRepositoryAdapter implements BlogSeriesRepositoryPort {
    private final BlogSeriesRepository blogSeriesRepository;


    @Override
    public Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series) {
        return blogSeriesRepository.findByBlogAndSeries(blog, series);
    }

    @Override
    public List<BlogSeriesEntity> findAllWithRelationsForCache() {
        return blogSeriesRepository.findAllWithRelationsForCache();
    }

    @Override
    public List<BlogSeriesEntity> findAll() {
        return blogSeriesRepository.findAll();
    }

    @Override
    public BlogSeriesEntity save(BlogSeriesEntity entity) {
        return blogSeriesRepository.save(entity);
    }
}
