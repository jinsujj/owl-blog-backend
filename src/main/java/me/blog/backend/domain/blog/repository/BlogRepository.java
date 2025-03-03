package me.blog.backend.domain.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.domain.blog.entity.BlogEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByAuthor(String author);
    Optional<BlogEntity> findByType(String type);
}
