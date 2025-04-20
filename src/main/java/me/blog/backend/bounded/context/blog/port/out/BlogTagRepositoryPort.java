package me.blog.backend.bounded.context.blog.port.out;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;

import java.util.List;

public interface BlogTagRepositoryPort {
    List<BlogTagEntity> findByBlog(BlogEntity blog);
    List<BlogTagEntity> findByTag(TagEntity tag);
    List<BlogTagEntity> findAll();
}
