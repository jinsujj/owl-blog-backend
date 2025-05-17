package me.blog.backend.bounded.context.blog.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogSeriesRepositoryAdapter;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.domain.vo.BlogSeriesVO;
import me.blog.backend.bounded.context.blog.port.out.cache.BlogSeriesCachePort;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogSeriesCache extends RedisAbstractCache<BlogSeriesVO> implements BlogSeriesCachePort {
  private final RedisTemplate<String, BlogSeriesVO> redisTemplate;
  private final BlogSeriesRepositoryAdapter blogSeriesRepository;

  @Override
  protected RedisTemplate<String, BlogSeriesVO> redisTemplate() {
    return redisTemplate;
  }

  @Override
  protected String getKeyPrefix() {
    return "series";
  }

  @Override
  protected String getListKey() {
    return "series:all";
  }

  @Override
  protected List<BlogSeriesVO> loadSource() {
    List<BlogSeriesVO> result = new ArrayList<>();
    List<BlogSeriesEntity> list = blogSeriesRepository.findAll();
    for (BlogSeriesEntity entity : list) {
      result.add(BlogSeriesVO.fromEntity(entity));
    }
    return result;
  }

  @Override
  protected Long getId(BlogSeriesVO item) {
    return item.id();
  }
}
