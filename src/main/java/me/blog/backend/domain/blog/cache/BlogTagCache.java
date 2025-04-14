package me.blog.backend.domain.blog.cache;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogTagEntity;
import me.blog.backend.domain.blog.entity.TagEntity;
import me.blog.backend.domain.blog.repository.BlogTagRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogTagCache extends AbstractCache<BlogTagEntity>{
    private final BlogTagRepository blogTagRepository;

    public BlogTagCache(BlogTagRepository blogTagRepository) {
        this.blogTagRepository = blogTagRepository;
    }

    public List<BlogTagEntity> findByBlog(BlogEntity blog) {
        return immutableList.stream()
                .filter(b -> b.getBlog().equals(blog))
                .collect(Collectors.toList());
    }

    public List<BlogTagEntity> findByTag(TagEntity tag) {
        return immutableList.stream()
                .filter(b -> b.getTag().equals(tag))
                .collect(Collectors.toList());
    }

    @Override
    public void putAll() {
        List<BlogTagEntity> blogTapList = blogTagRepository.findAll();
        immutableList = List.copyOf(blogTapList);
        isCached = true;
    }
}
