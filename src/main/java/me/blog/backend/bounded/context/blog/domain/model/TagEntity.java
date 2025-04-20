package me.blog.backend.bounded.context.blog.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String value;
  @Column(nullable = false, unique = true)
  private String label;

  public TagEntity(String value,String label) {
    this.value = value;
    this.label = label;
  }
}
