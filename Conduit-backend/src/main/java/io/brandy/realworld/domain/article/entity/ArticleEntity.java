package io.brandy.realworld.domain.article.entity;

import io.brandy.realworld.domain.tag.entity.TagArticleRelationEntity;
import io.brandy.realworld.domain.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles")
@NamedEntityGraph(name = "fetch-author-tagList", attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("tagList")})
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String body;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TagArticleRelationEntity> tagList;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FavoriteEntity> favoriteList;

    @Builder
    public ArticleEntity(Long id, String slug, String title, String description, String body, UserEntity author) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
    }


}
