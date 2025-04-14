package me.blog.backend.domain.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogSeriesEntity;
import me.blog.backend.domain.blog.entity.SeriesEntity;
import org.springframework.data.jpa.repository.Query;

public interface BlogSeriesRepository extends JpaRepository<BlogSeriesEntity, Long> {
  Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series);

  @EntityGraph(attributePaths = {"blog", "blog.blogTags", "blog.series", "blog.blogTags.tag","series"})
  @Query("SELECT b FROM BlogSeriesEntity b")
  List<BlogSeriesEntity> findAllWithRelationsForCache();
}
