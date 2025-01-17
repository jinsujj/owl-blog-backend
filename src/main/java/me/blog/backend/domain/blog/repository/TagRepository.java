package me.blog.backend.domain.blog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.domain.blog.entity.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long>{
  List<TagEntity> findByName(String name);
}
