package me.blog.backend.bounded.context.blog.adapter.out.database;

import me.blog.backend.bounded.context.blog.domain.model.BlogEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {
    List<BlogEntity> findByAuthor(String author);
    Optional<BlogEntity> findByType(String type);


    @EntityGraph(attributePaths = {"blogTags", "blogTags.tag", "series"})
    @Query("SELECT b FROM BlogEntity b")
    List<BlogEntity> findAllWithRelationsForCache();

    @Modifying
    @Query("UPDATE BlogEntity b SET b.readCount = b.readCount + 1 WHERE b.id = :id")
    void incrementReadCount(@Param("id") Long id);
}
