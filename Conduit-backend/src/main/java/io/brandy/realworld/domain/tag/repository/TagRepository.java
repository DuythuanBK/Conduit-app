package io.brandy.realworld.domain.tag.repository;

import io.brandy.realworld.domain.tag.entity.TagArticleRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagArticleRelationEntity, Long> {
}
