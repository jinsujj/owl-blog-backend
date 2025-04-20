package me.blog.backend.bounded.context.blog.adapter.out.cache;


import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
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
