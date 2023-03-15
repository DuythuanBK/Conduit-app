package io.brandy.realworld.domain.article.service;

import io.brandy.realworld.domain.article.dto.CommentDto;
import io.brandy.realworld.domain.user.dto.UserDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentList(String slug, UserDto.Auth authUser);

    CommentDto createComment(String slug, CommentDto.CreateComment createComment ,UserDto.Auth authUser);
    void deleteComment(String slug, Long id, UserDto.Auth authUser);

}
