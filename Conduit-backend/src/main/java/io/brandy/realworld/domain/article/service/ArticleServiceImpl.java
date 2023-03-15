package io.brandy.realworld.domain.article.service;

import io.brandy.realworld.domain.article.dto.ArticleDto;
import io.brandy.realworld.domain.article.entity.ArticleEntity;
import io.brandy.realworld.domain.article.entity.FavoriteEntity;
import io.brandy.realworld.domain.article.model.ArticleQueryParam;
import io.brandy.realworld.domain.article.model.FeedParams;
import io.brandy.realworld.domain.article.repository.ArticleRepository;
import io.brandy.realworld.domain.article.repository.FavoriteRepository;
import io.brandy.realworld.domain.profile.entity.FollowsEntity;
import io.brandy.realworld.domain.profile.repository.FollowsRepository;
import io.brandy.realworld.domain.profile.service.ProfileService;
import io.brandy.realworld.domain.tag.entity.TagArticleRelationEntity;
import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.entity.UserEntity;
import io.brandy.realworld.domain.user.repository.UserRepository;
import io.brandy.realworld.expection.AppException;
import io.brandy.realworld.expection.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final FollowsRepository followsRepository;

    private final ProfileService profileService;

    @Override
    public List<ArticleDto> getArticlesList(ArticleQueryParam param, UserDto.Auth authuser) {
        Pageable pageable = null;
        if(param.getOffset() != null) {
            pageable = PageRequest.of(param.getOffset(), param.getLimit());
        }
        List<ArticleEntity> articleEntities;
        if(param.getTag() != null) {
            articleEntities = articleRepository.findByTag(param.getTag(), pageable);
        } else if(param.getAuthor() != null ) {
            articleEntities = articleRepository.findByAuthorName(param.getAuthor(), pageable);
        } else if(param.getFavorite() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(param.getFavorite(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return convertToArticleList(articleEntities, authuser);
    }

    private List<ArticleDto> convertToArticleList(List<ArticleEntity> articleEntities, UserDto.Auth authUser) {
        List<Long> authorIds = articleEntities.stream()
                .map(ArticleEntity::getAuthor)
                .map(UserEntity::getId)
                .collect(Collectors.toList());
        if(authUser != null) {
            List<Long> followingIds = followsRepository.findByFollowerIdAndFollowingIdIn(authUser.getId(), authorIds)
                    .stream()
                    .map(FollowsEntity::getFollowing)
                    .map(UserEntity::getId)
                    .collect(Collectors.toList());

            return articleEntities.stream().map(entity -> {
                List<FavoriteEntity> favorites = entity.getFavoriteList();
                    Boolean favorited = favorites.stream()
                    .anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUser.getId()));

            int favoriteCount = favorites.size();

                    Boolean following = followingIds.stream()
                    .anyMatch(followeeId -> followeeId.equals(entity.getAuthor().getId()));

            return convertArticleEntityToDto(entity, favorited, (long) favoriteCount, following);

            }).collect(Collectors.toList());

        } else {
            return articleEntities.stream().map(entity -> {
                List<FavoriteEntity> favorites = entity.getFavoriteList();

                int favoriteCount = favorites.size();

                return convertArticleEntityToDto(entity, false, (long) favoriteCount, false);
            }).collect(Collectors.toList());
        }
    }

    @Override
    public List<ArticleDto> getArticlesFeed(FeedParams param, UserDto.Auth authuser) {
        Pageable pageable = null;
        if(param.getOffset() != null) {
            pageable = PageRequest.of(param.getOffset(), param.getLimit());
        }
        List<ArticleEntity> articleEntities;

        articleEntities = articleRepository.findByFeedUsername(authuser.getName(), pageable);

        return convertToArticleList(articleEntities, authuser);
    }

    @Override
    public ArticleDto getArticle(String slug, UserDto.Auth authuser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        List<FavoriteEntity> favorites = articleEntity.getFavoriteList();
        int favoritesCount = favorites.size();
        if(authuser != null) {
            Boolean following = profileService.getProfile(articleEntity.getAuthor().getName(), authuser).getFollowing();
            Boolean favorite = favorites.stream()
                    .anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authuser.getId()));
            return convertArticleEntityToDto(articleEntity, favorite, (long) favoritesCount, following);
        } else {
            return convertArticleEntityToDto(articleEntity, false, (long) favoritesCount, false);
        }
    }

    @Override
    public ArticleDto createArticle(ArticleDto.CreateArticle createArticle, UserDto.Auth authuser) {
        log.info(createArticle.getTagList().toString());
        String slug = String.join("-", createArticle.getTitle().split(" "));
        UserEntity author = UserEntity.builder()
                .id(authuser.getId())
                .name(authuser.getName())
                .email(authuser.getEmail())
                .bio(authuser.getBio())
                .image(authuser.getImage())
                .build();

        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(slug)
                .title(createArticle.getTitle())
                .description(createArticle.getDescription())
                .body(createArticle.getBody())
                .author(author)
                .build();

        articleEntity.setCreatedAt(LocalDateTime.now());
        articleEntity.setUpdatedAt(LocalDateTime.now());
        List<TagArticleRelationEntity> tagList = new ArrayList<>();

        for(String tag : createArticle.getTagList()) {
            tagList.add(TagArticleRelationEntity.builder().article(articleEntity).tag(tag).build());
        }

        articleEntity.setTagList(tagList);

        articleEntity = articleRepository.save(articleEntity);
        return convertArticleEntityToDto(articleEntity, false, 0L, false);
    }

    @Override
    public ArticleDto updateArticle(String slug, ArticleDto.UpdateArticle updateArticle, UserDto.Auth authuser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .filter(entity -> entity.getAuthor().getId().equals(authuser.getId()))
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        Boolean following = profileService.getProfile(articleEntity.getAuthor().getName(), authuser)
                .getFollowing();
        List<FavoriteEntity> favorites = articleEntity.getFavoriteList();
        Boolean favorite = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity
                .getUser().getId().equals(authuser.getId()));

        int favoritesCount = favorites.size();

        if(updateArticle.getBody() != null) {
            articleEntity.setBody(updateArticle.getBody());
        }

        if(updateArticle.getTitle() != null) {
            articleEntity.setTitle(updateArticle.getTitle());
        }

        if(updateArticle.getDescription() != null) {
            articleEntity.setDescription(updateArticle.getDescription());
        }

        articleEntity.setUpdatedAt(LocalDateTime.now());

        articleRepository.save(articleEntity);

        return convertArticleEntityToDto(articleEntity, favorite, (long) favoritesCount,following);
    }

    @Override
    public void deleteArticle(String slug, UserDto.Auth authuser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .filter(entity -> entity.getAuthor().getId().equals(authuser.getId()))
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        articleRepository.delete(articleEntity);
    }

    @Override
    public ArticleDto favoriteArticle(String slug, UserDto.Auth authuser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(authuser.getId())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = profileService.getProfile(articleEntity.getAuthor().getName(), authuser).getFollowing();
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setArticle(articleEntity);
        favoriteEntity.setUser(userEntity);

        favoriteRepository.save(favoriteEntity);

        return convertArticleEntityToDto(articleEntity,
                true,
                (long) articleEntity.getFavoriteList().size(),
                following );
    }

    @Override
    public ArticleDto unfavoriteArticle(String slug, UserDto.Auth authuser) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        UserEntity userEntity = userRepository.findById(authuser.getId())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));

        Boolean following = profileService.getProfile(articleEntity.getAuthor().getName(), authuser).getFollowing();
        Optional<FavoriteEntity> favoriteEntity = favoriteRepository
                .findByArticleIdAndUserId(articleEntity.getId(), userEntity.getId());

        favoriteRepository.delete(favoriteEntity.get());

        return convertArticleEntityToDto(articleEntity,
                false,
                (long) articleEntity.getFavoriteList().size(),
                following );
    }

    private ArticleDto convertArticleEntityToDto(ArticleEntity entity, Boolean favorite, Long favoritesCount, Boolean following) {
        return ArticleDto.builder()
                .slug(entity.getSlug())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .tagList(entity.getTagList().stream().map(TagArticleRelationEntity::getTag).collect(Collectors.toList()))
                .body(entity.getBody())
                .author(ArticleDto.Author.builder()
                        .username(entity.getAuthor().getName())
                        .bio(entity.getAuthor().getBio())
                        .image(entity.getAuthor().getImage())
                        .following(following)
                        .build())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .favorited(favorite)
                .favoritesCount(favoritesCount)
                .build();
    }
}
