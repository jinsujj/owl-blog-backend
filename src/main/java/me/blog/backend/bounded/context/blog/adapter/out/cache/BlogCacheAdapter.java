package me.blog.backend.bounded.context.blog.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogRepositoryAdapter;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogCachePort;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogCacheAdapter extends RedisAbstractCache<BlogVO> implements BlogCachePort {
    private final RedisTemplate<String, BlogVO> redisTemplate;
    private final BlogRepositoryAdapter blogRepository;

    @Override
    protected RedisTemplate<String, BlogVO> redisTemplate() {
        return redisTemplate;
    }

    @Override
    protected String getKeyPrefix() {
        return "blog";
    }

    @Override
    protected String getListKey() {
        return "blog:all";
    }

    @Override
    protected List<BlogVO> loadSource() {
        List<BlogEntity> list = blogRepository.findAllWithRelationsForCache();
        List<BlogVO> result = new ArrayList<>();
        for (BlogEntity blogEntity : list) {
            result.add(BlogVO.fromEntity(blogEntity));
        }
        return result;
    }

    @Override
    protected Long getId(BlogVO item) {
        return item.id();
    }

    // custom method
    public List<BlogVO> findByAuthor(String author) {
        return findAll().stream()
            .filter(blog -> blog.author().equals(author))
            .toList();
    }

    public Optional<BlogVO> findByType(String type) {
        return findAll().stream()
            .filter(blog -> blog.type().equals(type))
            .findFirst();
    }
}
