package me.blog.backend.bounded.context.blog.adapter.out.database;

import lombok.RequiredArgsConstructor;
import me.blog.backend.bounded.context.blog.domain.model.TagEntity;
import me.blog.backend.bounded.context.blog.port.out.TagRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepositoryAdapter implements TagRepositoryPort {
    private final TagRepository tagRepository;

    @Override
    public List<TagEntity> findByValue(String value) {
        return tagRepository.findByValue(value);
    }

    @Override
    public TagEntity save(TagEntity tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void delete(TagEntity tag) {
        tagRepository.delete(tag);
    }

    @Override
    public List<TagEntity> findAll() {
        return tagRepository.findAll();
    }
}
