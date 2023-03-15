package io.brandy.realworld.domain.article.service;

import io.brandy.realworld.domain.article.dto.ArticleDto;
import io.brandy.realworld.domain.article.entity.ArticleEntity;
import io.brandy.realworld.domain.article.model.ArticleQueryParam;
import io.brandy.realworld.domain.article.model.FeedParams;
import io.brandy.realworld.domain.user.dto.UserDto;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> getArticlesList(ArticleQueryParam param, UserDto.Auth authuser);

    List<ArticleDto> getArticlesFeed(FeedParams param, UserDto.Auth authuser);

    ArticleDto getArticle(String slug, UserDto.Auth authuser);

    ArticleDto createArticle(ArticleDto.CreateArticle createArticle, UserDto.Auth authuser);

    ArticleDto updateArticle(String slug, ArticleDto.UpdateArticle updateArticle, UserDto.Auth authuser);

    void deleteArticle(String slug, UserDto.Auth authuser);

    ArticleDto favoriteArticle(String slug, UserDto.Auth authuser);

    ArticleDto unfavoriteArticle(String slug, UserDto.Auth authuser);

}
