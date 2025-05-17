package me.blog.backend.bounded.context.blog.adapter.in.batch;

import lombok.extern.slf4j.Slf4j;
import me.blog.backend.bounded.context.blog.adapter.out.cache.RedisAbstractCache;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CacheManagerAdapter {
    private final List<RedisAbstractCache<?>> caches;

    public CacheManagerAdapter(List<RedisAbstractCache<?>> caches) {
        this.caches = caches;
        refreshAllCaches();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void refreshAllCaches(){
        caches.forEach(cache -> {
            log.info(resolveGenericTypeName(cache));
            cache.putAll();
        });
    }

    private String resolveGenericTypeName(RedisAbstractCache<?> cache) {
        if(!cache.getClass().getSimpleName().isBlank()) {
            return cache.getClass().getSimpleName() + " cached";
        }
        return "UnknownType (empty cache)";
    }
}
