package io.brandy.realworld.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.brandy.realworld.domain.user.dto.UserDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ArticleDto {
    private String slug;

    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String body;
    private List<String> tagList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean favorited;
    private Long favoritesCount;
    private Author author;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Author {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonTypeName("article")
    public static class SigleArticle {
        private ArticleDto article;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonTypeName("articles")
    public static class MultipleArticles {
        private List<ArticleDto> articles;
        private Long articlesCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonTypeName("article")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class UpdateArticle {
        @JsonProperty("title")
        private String title;
        @JsonProperty("description")
        private String description;
        @JsonProperty("body")
        private String body;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonTypeName("article")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class CreateArticle {
        private String title;
        private String description;
        private String body;
        private List<String> tagList;
    }
}
