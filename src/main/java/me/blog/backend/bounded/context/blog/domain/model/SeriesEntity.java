package me.blog.backend.bounded.context.blog.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "series")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeriesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  private String name;

  public SeriesEntity(String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
  }
}
