package me.blog.backend.domain.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.blog.backend.domain.blog.entity.BlogEntity;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByAuthor(String author);
}
