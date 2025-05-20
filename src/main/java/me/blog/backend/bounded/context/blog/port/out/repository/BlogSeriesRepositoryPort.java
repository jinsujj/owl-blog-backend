package me.blog.backend.bounded.context.blog.port.out.repository;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;

import java.util.List;
import java.util.Optional;

public interface BlogSeriesRepositoryPort {
    Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series);
    List<BlogSeriesEntity> findBySeries(SeriesEntity series);
    List<BlogSeriesEntity> findAllWithRelationsForCache();
    List<BlogSeriesEntity> findAll();
    void deleteAllBySeries(SeriesEntity series);
    BlogSeriesEntity save(BlogSeriesEntity entity);
}
