package io.brandy.realworld.domain.article.controller;

import io.brandy.realworld.domain.article.dto.ArticleDto;
import io.brandy.realworld.domain.article.dto.CommentDto;
import io.brandy.realworld.domain.article.model.ArticleQueryParam;
import io.brandy.realworld.domain.article.model.FeedParams;
import io.brandy.realworld.domain.article.service.ArticleService;
import io.brandy.realworld.domain.article.service.CommentService;
import io.brandy.realworld.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/articles")
@CrossOrigin
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/{slug}")
    public ArticleDto.SigleArticle readArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal UserDto.Auth authUser) {
        return ArticleDto.SigleArticle.builder().article(articleService.getArticle(slug, authUser)).build();
    }

    @PostMapping
    public ArticleDto.SigleArticle createArticle(@RequestBody ArticleDto.CreateArticle createArticle ,
                                    @AuthenticationPrincipal UserDto.Auth authUser) {

        return ArticleDto.SigleArticle.builder().article(articleService.createArticle(createArticle, authUser)).build();
    }

    @PutMapping("/{slug}")
    public ArticleDto.SigleArticle updateArticles(@PathVariable("slug") String slug,
                                     @RequestBody ArticleDto.UpdateArticle updateArticle,
                                     @AuthenticationPrincipal UserDto.Auth authUser) {
        return ArticleDto.SigleArticle.builder().article(articleService.updateArticle(slug, updateArticle, authUser)).build();
    }

    @DeleteMapping("/{slug}")
    public void deleteArticles(@PathVariable("slug") String slug,
                                     @AuthenticationPrincipal UserDto.Auth authUser) {
        articleService.deleteArticle(slug, authUser);
    }

    @GetMapping
    public ArticleDto.MultipleArticles getListArticles(@ModelAttribute ArticleQueryParam param,
                                                      @AuthenticationPrincipal UserDto.Auth authUser) {
        ArticleDto.MultipleArticles MultipleArticles = new ArticleDto.MultipleArticles();
        MultipleArticles.setArticles(articleService.getArticlesList(param, authUser));
        MultipleArticles.setArticlesCount((long)MultipleArticles.getArticles().size());

        return MultipleArticles;
    }

    @GetMapping("/feed")
    public ArticleDto.MultipleArticles getFeedArticles(@ModelAttribute FeedParams param,
                                                      @AuthenticationPrincipal UserDto.Auth authUser) {
        ArticleDto.MultipleArticles MultipleArticles = new ArticleDto.MultipleArticles();
        MultipleArticles.setArticles(articleService.getArticlesFeed(param, authUser));
        MultipleArticles.setArticlesCount((long)MultipleArticles.getArticles().size());

        return MultipleArticles;
    }


    @PostMapping("/{slug}/comments")
    public CommentDto addComment(@PathVariable("slug") String slug,
                                 @RequestBody CommentDto.CreateComment createComment,
                                 @AuthenticationPrincipal UserDto.Auth authUser) {
        return commentService.createComment(slug, createComment, authUser);
    }

    @GetMapping("/{slug}/comments")
    public CommentDto.MutipleComment getComments(@PathVariable("slug") String slug,
                                    @AuthenticationPrincipal UserDto.Auth authUser) {
        return CommentDto.MutipleComment.builder().comments(commentService.getCommentList(slug, authUser)).build();
    }

    @DeleteMapping("/{slug}/comments/{id}")
    public void deleteComment(@PathVariable("slug") String slug,
                                    @PathVariable("id") Long id,
                                    @AuthenticationPrincipal UserDto.Auth authUser) {
        commentService.deleteComment(slug, id, authUser);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleDto.SigleArticle favoriteArticle(@PathVariable("slug") String slug,
                                      @AuthenticationPrincipal UserDto.Auth authUser) {
        return ArticleDto.SigleArticle.builder().article(articleService.favoriteArticle(slug, authUser)).build();
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleDto.SigleArticle unfavoriteArticle(@PathVariable("slug") String slug,
                                        @AuthenticationPrincipal UserDto.Auth authUser) {
        return  ArticleDto.SigleArticle.builder().article(articleService.unfavoriteArticle(slug, authUser)).build();
    }

}
