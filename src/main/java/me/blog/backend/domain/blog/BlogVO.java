package me.blog.backend.domain.blog;

import java.time.LocalDateTime;
import java.util.List;

public record BlogVO(Long id, String title, String content, List<String> attachments, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt) {
  public static BlogVO fromEntity(BlogEntity entity) {
    return new BlogVO(entity.getId(), entity.getTitle(), entity.getContent(), entity.getAttachments(),
        entity.getCreatedAt(), entity.getUpdatedAt(), entity.getPublishedAt());
  }
}