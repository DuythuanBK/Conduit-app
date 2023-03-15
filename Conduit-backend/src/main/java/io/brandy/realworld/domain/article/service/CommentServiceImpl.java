package io.brandy.realworld.domain.article.service;

import io.brandy.realworld.domain.article.dto.CommentDto;
import io.brandy.realworld.domain.article.entity.ArticleEntity;
import io.brandy.realworld.domain.article.entity.CommentEntity;
import io.brandy.realworld.domain.article.repository.ArticleRepository;
import io.brandy.realworld.domain.article.repository.CommentRepository;
import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.expection.AppException;
import io.brandy.realworld.expection.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> getCommentList(String slug, UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository
                .findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        List<CommentEntity> commentEntities =  commentRepository.findByArticleId(articleEntity.getId());
        List<CommentDto> commentDtos = new ArrayList<>();

        commentDtos = commentEntities.stream().map((commentEntity) -> {
            return CommentDto.builder()
                            .id(commentEntity.getId())
                            .body(commentEntity.getBody())
                            .createdAt(commentEntity.getCreatedAt())
                            .updatedAt(commentEntity.getUpdatedAt())
                            .author(CommentDto.Author.builder()
                                    .username(commentEntity.getAuthor().getName())
                                    .image(commentEntity.getAuthor().getImage())
                                    .bio(commentEntity.getAuthor().getBio())
                                    .following(false)
                                    .build())
                            .build();
        }).collect(Collectors.toList());

        return commentDtos;
    }

    @Override
    public CommentDto createComment(String slug,
                                    CommentDto.CreateComment createComment,
                                    UserDto.Auth authUser) {
        ArticleEntity articleEntity = articleRepository
                .findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setUpdatedAt(LocalDateTime.now());
        commentEntity.setBody(createComment.getBody());
        commentEntity.setArticle(articleEntity);
        commentEntity.setAuthor(articleEntity.getAuthor());

        commentEntity = commentRepository.save(commentEntity);

        return convertEntityToDto(commentEntity, false);
    }

    private CommentDto convertEntityToDto(CommentEntity commentEntity, Boolean following) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .body(commentEntity.getBody())
                .author(CommentDto.Author.builder()
                        .username(commentEntity.getAuthor().getName())
                        .bio(commentEntity.getAuthor().getBio())
                        .image(commentEntity.getAuthor().getImage())
                        .following(following)
                        .build())
                .build();
    }

    @Override
    public void deleteComment(String slug, Long id, UserDto.Auth authUser) {
        CommentEntity commentEntity = commentRepository
                .findById(id).orElseThrow(( ) -> new AppException(Error.COMMENT_NOT_FOUND));

        commentRepository.delete(commentEntity);
    }
}
