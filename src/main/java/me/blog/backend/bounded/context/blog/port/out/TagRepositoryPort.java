package me.blog.backend.bounded.context.blog.port.out;

import me.blog.backend.bounded.context.blog.domain.model.TagEntity;

import java.util.List;

public interface TagRepositoryPort {
    List<TagEntity> findByValue(String value);
    TagEntity save(TagEntity tag);
    void delete(TagEntity tag);
    List<TagEntity> findAll();
}
