package io.brandy.realworld.domain.article.entity;

import io.brandy.realworld.domain.article.entity.ArticleEntity;
import io.brandy.realworld.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "fetch-author", attributeNodes = @NamedAttributeNode("author"))
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;


}
