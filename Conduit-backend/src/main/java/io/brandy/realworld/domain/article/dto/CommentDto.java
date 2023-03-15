package io.brandy.realworld.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.brandy.realworld.domain.user.dto.UserDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    private String body;

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
    @JsonTypeName("comment")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class SigleComment {
        private CommentDto comment;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MutipleComment {
        private List<CommentDto> comments;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonTypeName("comment")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class CreateComment {
        private String body;
    }
}
