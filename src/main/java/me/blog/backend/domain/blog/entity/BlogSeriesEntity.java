package me.blog.backend.domain.blog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "blog_series")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogSeriesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name ="created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "blog_id", nullable = false)
  private BlogEntity blog;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "series_id", nullable = false)
  private SeriesEntity series;

  public BlogSeriesEntity(BlogEntity blog, SeriesEntity series) {
    this.blog = blog;
    this.series = series;
    this.createdAt = LocalDateTime.now();
  }
}
