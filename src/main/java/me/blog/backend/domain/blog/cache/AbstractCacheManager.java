package me.blog.backend.domain.blog.cache;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbstractCacheManager {
    private final List<AbstractCache<?>> caches;

    public AbstractCacheManager(List<AbstractCache<?>> caches) {
        this.caches = caches;
        refreshAllCaches();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void refreshAllCaches(){
        caches.forEach(cache -> {
            cache.evictAll();
            cache.putAll();
        });
    }
}
