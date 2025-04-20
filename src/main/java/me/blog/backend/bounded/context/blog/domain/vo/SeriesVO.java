package me.blog.backend.bounded.context.blog.domain.vo;

import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;

public record SeriesVO(String name) {
  public static SeriesVO from(SeriesEntity series) {
    return new SeriesVO(series.getName());
  }
}
