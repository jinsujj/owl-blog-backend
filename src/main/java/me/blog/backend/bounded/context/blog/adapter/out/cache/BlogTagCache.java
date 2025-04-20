package me.blog.backend.bounded.context.blog.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogTagRepositoryAdapter;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogTagEntity;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogTagRepository;
import me.blog.backend.bounded.context.blog.port.out.BlogTagCachePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BlogTagCache extends AbstractCache<BlogTagEntity> implements BlogTagCachePort {
    private final BlogTagRepositoryAdapter blogTagRepository;

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
