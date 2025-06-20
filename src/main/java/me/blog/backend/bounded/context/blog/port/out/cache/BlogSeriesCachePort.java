package me.blog.backend.bounded.context.blog.port.out.cache;

import me.blog.backend.bounded.context.blog.domain.vo.BlogSeriesVO;

import java.util.List;

public interface BlogSeriesCachePort {
    void putAll();
    List<BlogSeriesVO> findAll();
}
