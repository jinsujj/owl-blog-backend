package me.blog.backend.domain.blog.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogSeriesEntity;
import me.blog.backend.domain.blog.entity.SeriesEntity;

public interface BlogSeriesRepository extends JpaRepository<BlogSeriesEntity, Long> {
  Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series);
}
