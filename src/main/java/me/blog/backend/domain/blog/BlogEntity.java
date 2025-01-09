package me.blog.backend.domain.blog;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name ="blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String content;

  @ElementCollection
  @CollectionTable(name ="blog_attachments", joinColumns = @JoinColumn(name ="blog_id"))
  private List<String> attachments;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  @Column(name= "published_at", nullable = true)
  private LocalDateTime publishedAt;


 public boolean isPublished() {
    return publishedAt != null && publishedAt.isBefore(LocalDateTime.now());
  }

  public void publish() {
    this.publishedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void unpublish() {
    this.publishedAt = null;
    this.updatedAt = LocalDateTime.now();
  }
  public BlogEntity(String title, String content, List<String> attachments, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt) {
    this.title = title;
    this.content = content;
    this.attachments = attachments;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.publishedAt = publishedAt;
  }
}
