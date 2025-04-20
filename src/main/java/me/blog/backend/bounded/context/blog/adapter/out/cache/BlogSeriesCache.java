package me.blog.backend.bounded.context.blog.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.adapter.out.database.BlogSeriesRepositoryAdapter;
import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import me.blog.backend.bounded.context.blog.domain.model.BlogSeriesEntity;
import me.blog.backend.bounded.context.blog.domain.model.SeriesEntity;
import me.blog.backend.bounded.context.blog.port.out.BlogSeriesCachePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlogSeriesCache extends AbstractCache<BlogSeriesEntity> implements BlogSeriesCachePort {
    private final BlogSeriesRepositoryAdapter blogSeriesRepository;

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
