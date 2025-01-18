package me.blog.backend.domain.blog.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
  private int readCount;

  @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<BlogTagEntity> blogTags = new HashSet<>();
  @Lob
  @Column(columnDefinition = "TEXT")
  private String content;
  @Lob
  @Column(columnDefinition = "TEXT")
  private String summary;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  @Column(name= "published_at", nullable = true)
  private LocalDateTime publishedAt;
  private String thumbnailUrl;

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

  public void addTag(TagEntity tag) {
    boolean alreadyExists = blogTags.stream()
        .anyMatch(blogTag -> blogTag.getTag().equals(tag));

    if(!alreadyExists) {
      this.blogTags.add(new BlogTagEntity(this, tag));
    }
  }

  public void removeTag(TagEntity tag) {
    blogTags.removeIf(blogTag -> blogTag.getTag().equals(tag));
  }

  public void upLoadThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public void removeThumbnailUrl() {
    this.thumbnailUrl = null;
  }
}
