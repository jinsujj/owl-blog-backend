package me.blog.backend.bounded.context.blog.domain.vo;

import java.time.LocalDateTime;
import java.util.Set;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;

public record BlogVO(Long id, String author, String title, String content, String summary, int readCount, String thumbnailUrl, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt, TagVO[] tags) {
  public static BlogVO fromEntity(BlogEntity entity) {
    Set<BlogTagEntity> blogTags = entity.getBlogTags();
    return new BlogVO(
            entity.getId(),
            entity.getAuthor(),
            entity.getTitle(),
            entity.getContent(),
            entity.getSummary(),
            entity.getReadCount(),
            entity.getThumbnailUrl(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getPublishedAt(),
            TagVO.from(blogTags)
    );
  }
}