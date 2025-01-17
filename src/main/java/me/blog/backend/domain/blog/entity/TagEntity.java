package me.blog.backend.domain.blog.entity;

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
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

	private String name;

  private String label;

  public TagEntity(String name,String label) {
		this.name = name;
    this.label = label;
  }
}
