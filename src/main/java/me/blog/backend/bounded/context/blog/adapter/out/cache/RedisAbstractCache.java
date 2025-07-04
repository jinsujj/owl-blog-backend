package me.blog.backend.bounded.context.blog.adapter.out.cache;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class RedisAbstractCache<T>{
    protected abstract RedisTemplate<String, T> redisTemplate();
    protected abstract String getKeyPrefix(); // e.g., "blog", "series"
    protected abstract String getListKey();   // e.g., "blog:all"
    protected abstract List<T> loadSource();  // child class's DB source
    protected abstract Long getId(T item);

    public List<T> findAll(){
        try {
            List<T> rawList = redisTemplate().opsForList().range(getListKey(), 0, -1);
            if (rawList == null || rawList.isEmpty())
                return List.of();

            return rawList;
        } catch (Exception e) {
            log.warn("Redis connection failed while fetching {} list", getKeyPrefix(), e);
            return List.of();
        }
    }

    public Optional<T> findById(Long id) {
        try {
            T value = redisTemplate().opsForValue().get(getKeyPrefix() + ":" + id);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            log.warn("Redis connection failed while fetching {}:{}", getKeyPrefix(), id, e);
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public void putAll() {
        try {
            // init
            List<T> dataList = loadSource();
            if (dataList == null || dataList.isEmpty()) return;

            // delete
            redisTemplate().delete(getListKey());
            Set<String> keys = redisTemplate().keys(getKeyPrefix() + ":*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate().delete(keys);
            }
            // update
            dataList.forEach(item -> {
                String key = getKeyPrefix() + ":" + getId(item);
                redisTemplate().opsForValue().set(key, item);
                redisTemplate().opsForList().rightPush(getKeyPrefix()+":all", item);
            });
        } catch (Exception e) {
            log.warn("Redis connection failed while updating {} cache", getKeyPrefix(), e);
        }
    }
}
