package me.blog.backend.domain.blog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogTagEntity;

@Repository
public interface BlogTagRepository extends JpaRepository<BlogTagEntity, Long> {
  List<BlogTagEntity> findByBlog(BlogEntity blog);
}
