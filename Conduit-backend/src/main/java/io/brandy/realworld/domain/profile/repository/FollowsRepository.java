package io.brandy.realworld.domain.profile.repository;

import io.brandy.realworld.domain.profile.entity.FollowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowsRepository extends JpaRepository<FollowsEntity, Long> {

    Optional<FollowsEntity> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

//    List<FollowsEntity> findByFollowerId(Long id);
    List<FollowsEntity> findByFollowerIdAndFollowingIdIn(Long id, List<Long> authorsId);
}
