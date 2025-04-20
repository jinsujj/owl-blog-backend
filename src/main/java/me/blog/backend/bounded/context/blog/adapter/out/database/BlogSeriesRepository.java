package me.blog.backend.bounded.context.blog.adapter.out.database;

import java.util.List;
import java.util.Optional;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

public interface BlogSeriesRepository extends JpaRepository<BlogSeriesEntity, Long> {
  Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series);

  @EntityGraph(attributePaths = {"blog", "blog.blogTags", "blog.series", "blog.blogTags.tag","series"})
  @Query("SELECT b FROM BlogSeriesEntity b")
  List<BlogSeriesEntity> findAllWithRelationsForCache();
}
