package me.blog.backend.bounded.context.blog.port.out.cache;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;

import java.util.List;
import java.util.Optional;

public interface BlogSeriesCachePort {
    Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series);
    void putAll();
    List<BlogSeriesEntity> findAll();

}
