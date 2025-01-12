package me.blog.backend.domain.blog.entitiy;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
  @Lob
  @Column(columnDefinition = "TEXT")
  private String content;
  private int readCount;

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

  public void readCounting(){
    this.readCount++;
  }

  public BlogEntity(String title, String content, int readCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt) {
    this.title = title;
    this.content = content;
    this.readCount = readCount;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.publishedAt = publishedAt;
  }

  public BlogEntity(String title, String content, LocalDateTime createdAt){
   this.title = title;
   this.content = content;
   this.readCount = 0;
   this.createdAt = createdAt;
   this.updatedAt = null;
   this.publishedAt = null;
  }

}
