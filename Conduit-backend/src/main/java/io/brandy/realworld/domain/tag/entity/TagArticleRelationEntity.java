package io.brandy.realworld.domain.tag.entity;

import io.brandy.realworld.domain.article.entity.ArticleEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class TagArticleRelationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;
}
