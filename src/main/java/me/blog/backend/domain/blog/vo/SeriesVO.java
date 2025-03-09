package me.blog.backend.domain.blog.vo;

import me.blog.backend.domain.blog.entity.SeriesEntity;

public record SeriesVO(String name) {
  public static SeriesVO from(SeriesEntity series) {
    return new SeriesVO(series.getName());
  }
}
