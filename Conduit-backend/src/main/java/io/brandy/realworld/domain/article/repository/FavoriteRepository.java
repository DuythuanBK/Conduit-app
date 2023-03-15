package io.brandy.realworld.domain.article.repository;

import io.brandy.realworld.domain.article.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
//    @Query("SELECT COUNT(f.id) FROM FavoriteEntity f WHERE f.article.id = :id")
//    Long findFavoritesCount(@Param("id") Long articleId);

    Optional<FavoriteEntity> findByArticleIdAndUserId(Long articleId, Long userId);
}
