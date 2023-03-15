package io.brandy.realworld.domain.profile.service;

import io.brandy.realworld.domain.profile.dto.ProfileDto;
import io.brandy.realworld.domain.profile.entity.FollowsEntity;
import io.brandy.realworld.domain.profile.repository.FollowsRepository;
import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.entity.UserEntity;
import io.brandy.realworld.domain.user.repository.UserRepository;
import io.brandy.realworld.expection.AppException;
import io.brandy.realworld.expection.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final FollowsRepository followsRepository;

    private ProfileDto covertFromUserEntity(UserEntity userEntity, Boolean following) {
        return ProfileDto.builder().username(userEntity.getName())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .following(following)
                .build();
    }

    public ProfileDto getProfile(String username, UserDto.Auth authUser) {
        Boolean following = false;

        Optional<UserEntity> follower = userRepository.findByName(authUser.getName());
        if(!follower.isPresent()) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        Optional<UserEntity> followingUser = userRepository.findByName(username);
        if (!followingUser.isPresent()) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        Optional<FollowsEntity> followingEntity =
                followsRepository.findByFollowerIdAndFollowingId(follower.get().getId(), followingUser.get().getId());

        if (followingEntity.isPresent()) {
            following = true;
        }

        return covertFromUserEntity(followingUser.get(), following);
    }

    public ProfileDto followUser(String username, UserDto.Auth authUser) {
        Optional<UserEntity> follower = userRepository.findByName(authUser.getName());

        Optional<UserEntity> followingUser = userRepository.findByName(username);

        if(!follower.isPresent() || !followingUser.isPresent() ) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        FollowsEntity followsEntity = new FollowsEntity();
        followsEntity.setFollower(follower.get());
        followsEntity.setFollowing(followingUser.get());

        followsRepository.save(followsEntity);
        return covertFromUserEntity(followingUser.get(), true);
    }

    public ProfileDto unfollowUser(String username, UserDto.Auth authUser) {
        Optional<UserEntity> follower = userRepository.findByName(authUser.getName());

        Optional<UserEntity> followingUser = userRepository.findByName(username);

        if(!follower.isPresent() || !followingUser.isPresent() ) {
            throw new AppException(Error.USER_NOT_FOUND);
        }

        Optional<FollowsEntity> followsEntity = followsRepository
                .findByFollowerIdAndFollowingId(follower.get().getId(), followingUser.get().getId());

        if(followsEntity.isPresent()) {
            followsRepository.delete(followsEntity.get());
        }

        return covertFromUserEntity(followingUser.get(), false);
    }
}
