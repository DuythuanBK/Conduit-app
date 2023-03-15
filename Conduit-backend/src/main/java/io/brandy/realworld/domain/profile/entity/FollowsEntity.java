package io.brandy.realworld.domain.profile.entity;

import io.brandy.realworld.domain.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint(name = "follower_following_pair_must_be_unique" ,columnNames = {"follower", "following"}))
public class FollowsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower", nullable = false)
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following", nullable = false)
    private UserEntity following;
}
