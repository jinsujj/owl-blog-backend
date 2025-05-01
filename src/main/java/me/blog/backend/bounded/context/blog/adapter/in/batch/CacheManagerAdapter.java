package me.blog.backend.bounded.context.blog.adapter.in.batch;

import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.adapter.out.cache.AbstractCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CacheManagerAdapter {
    private final List<AbstractCache<?>> caches;

    public CacheManagerAdapter(List<AbstractCache<?>> caches) {
        this.caches = caches;
        refreshAllCaches();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void refreshAllCaches(){
        caches.forEach(cache -> {
            log.info(resolveGenericTypeName(cache));
            cache.evictAll();
            cache.putAll();
        });
    }

    private String resolveGenericTypeName(AbstractCache<?> cache) {
        List<?> list = cache.findAll();
        if(!list.isEmpty()) {
            Object first = list.get(0);
            return first.getClass().getSimpleName() + " cached";
        }
        return "UnknownType (empty cache)";
    }
}
