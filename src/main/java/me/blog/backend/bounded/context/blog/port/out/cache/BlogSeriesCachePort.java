package me.blog.backend.bounded.context.blog.port.out.cache;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.domain.vo.BlogSeriesVO;

import java.util.List;
import java.util.Optional;

public interface BlogSeriesCachePort {
    void putAll();
    List<BlogSeriesVO> findAll();
}
