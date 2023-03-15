package io.brandy.realworld.domain.article.repository;

import io.brandy.realworld.domain.article.entity.CommentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @EntityGraph("fetch-author")
//    @Query("SELECT c FROM CommentEntity c WHERE c.article.id = :id ORDER BY c.createAt DESC")
    List<CommentEntity> findByArticleId(Long id);
}
