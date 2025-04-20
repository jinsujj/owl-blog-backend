package me.blog.backend.bounded.context.blog.adapter.out.database;

import java.util.List;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;

@Repository
public interface BlogTagRepository extends JpaRepository<BlogTagEntity, Long> {
  List<BlogTagEntity> findByBlog(BlogEntity blog);
  List<BlogTagEntity> findByTag(TagEntity tag);
}
