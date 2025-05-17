package me.blog.backend.bounded.context.blog.domain.vo;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;

public record BlogSeriesVO (
    Long id,
    String seriesName,
    BlogVO blog
){
  public static BlogSeriesVO fromEntity(BlogSeriesEntity entity) {
    SeriesEntity series = entity.getSeries();
    BlogEntity blogEntity = entity.getBlog();
    return new BlogSeriesVO(
        entity.getId(),
        series.getName(),
        BlogVO.fromEntity(blogEntity)
    );
  }
}
