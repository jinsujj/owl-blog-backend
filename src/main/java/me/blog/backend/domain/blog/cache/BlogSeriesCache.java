package me.blog.backend.domain.blog.cache;

import me.blog.backend.domain.blog.entity.BlogEntity;
import me.blog.backend.domain.blog.entity.BlogSeriesEntity;
import me.blog.backend.domain.blog.entity.SeriesEntity;
import me.blog.backend.domain.blog.repository.BlogSeriesRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class BlogSeriesCache extends AbstractCache<BlogSeriesEntity> {
    private final BlogSeriesRepository blogSeriesRepository;

    public BlogSeriesCache(BlogSeriesRepository blogSeriesRepository) {
        this.blogSeriesRepository = blogSeriesRepository;
    }

    public Optional<BlogSeriesEntity> findByBlogAndSeries(BlogEntity blog, SeriesEntity series) {
        return immutableList.stream()
                .filter(b -> b.getBlog().equals(blog) && b.getSeries().equals(series))
                .findFirst();
    }

    @Transactional(readOnly = true)
    @Override
    public void putAll() {
        List<BlogSeriesEntity> blogMaps = blogSeriesRepository.findAllWithRelationsForCache();
        immutableList = List.copyOf(blogMaps);
        isCached = true;
    }
}
