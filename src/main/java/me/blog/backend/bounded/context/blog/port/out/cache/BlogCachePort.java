package me.blog.backend.bounded.context.blog.port.out.cache;

import me.blog.backend.bounded.context.blog.domain.vo.BlogVO;

import java.util.List;
import java.util.Optional;

public interface BlogCachePort {
    List<BlogVO> findAll();
    List<BlogVO> findByAuthor(String author);
    Optional<BlogVO> findById(Long id);
    Optional<BlogVO> findByType(String type);
    void putAll();
}
