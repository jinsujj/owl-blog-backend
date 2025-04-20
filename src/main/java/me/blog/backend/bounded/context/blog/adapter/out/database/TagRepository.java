package me.blog.backend.bounded.context.blog.adapter.out.database;

import java.util.List;

import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long>{
  List<TagEntity> findByValue(String value);
}
