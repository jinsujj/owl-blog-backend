package me.blog.backend.bounded.context.blog.adapter.out.cache;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class RedisAbstractCache<T>{
    protected abstract RedisTemplate<String, T> redisTemplate();
    protected abstract String getKeyPrefix(); // e.g., "blog", "series"
    protected abstract String getListKey();   // e.g., "blog:all"
    protected abstract List<T> loadSource();  // child class's DB source
    protected abstract Long getId(T item);

    public List<T> findAll(){
        List<T> rawList = redisTemplate().opsForList().range(getListKey(), 0, -1);
        if (rawList == null || rawList.isEmpty())
            return List.of();

        return rawList;
    }

    public Optional<T> findById(Long id) {
        T value = redisTemplate().opsForValue().get(getKeyPrefix() + ":" + id);
        return Optional.ofNullable(value);
    }

    @Transactional(readOnly = true)
    public void putAll() {
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
    }
}
