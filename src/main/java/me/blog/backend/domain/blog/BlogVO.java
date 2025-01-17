package me.blog.backend.domain.blog;

import java.time.LocalDateTime;

import me.blog.backend.domain.blog.entity.BlogEntity;

public record BlogVO(Long id, String title, String content, int readCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt) {
  public static BlogVO fromEntity(BlogEntity entity) {
    return new BlogVO(entity.getId(), entity.getTitle(), entity.getContent(), entity.getReadCount(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getPublishedAt());
  }
}