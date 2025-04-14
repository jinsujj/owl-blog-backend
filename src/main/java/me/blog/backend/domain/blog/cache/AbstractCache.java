package me.blog.backend.domain.blog.cache;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractCache<T>{
    protected List<T> immutableList = List.of();
    protected volatile boolean isCached = false;

    public List<T> findAll(){
        return immutableList;
    }

    public boolean isCached() {
        return isCached;
    }

    public void evictAll(){
        immutableList = List.of();
        isCached = false;
    }

    public abstract void putAll();
}
