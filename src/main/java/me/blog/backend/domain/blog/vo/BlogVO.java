package me.blog.backend.domain.blog.vo;

import java.time.LocalDateTime;
import java.util.Set;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogTagEntity;

public record BlogVO(Long id, String title, String content, String summary, int readCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt, TagVO[] tags) {
  public static BlogVO fromEntity(BlogEntity entity) {
    Set<BlogTagEntity> blogTags = entity.getBlogTags();
    return new BlogVO(entity.getId(), entity.getTitle(), entity.getContent(), entity.getSummary(), entity.getReadCount(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getPublishedAt(), TagVO.from(blogTags));
  }
}