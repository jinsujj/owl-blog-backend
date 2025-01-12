package me.blog.backend.domain.blog;

import org.springframework.data.jpa.repository.JpaRepository;

import me.blog.backend.domain.blog.entitiy.BlogEntity;

public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
}
