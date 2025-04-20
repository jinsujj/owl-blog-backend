package me.blog.backend.bounded.context.blog.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import me.blog.backend.bounded.context.blog.port.out.repository.BlogTagRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlogTagRepositoryAdapter implements BlogTagRepositoryPort {
    private final BlogTagRepository blogTagRepository;


    @Override
    public List<BlogTagEntity> findByBlog(BlogEntity blog) {
        return blogTagRepository.findByBlog(blog);
    }

    @Override
    public List<BlogTagEntity> findByTag(TagEntity tag) {
        return blogTagRepository.findByTag(tag);
    }

    @Override
    public List<BlogTagEntity> findAll() {
        return blogTagRepository.findAll();
    }
}
