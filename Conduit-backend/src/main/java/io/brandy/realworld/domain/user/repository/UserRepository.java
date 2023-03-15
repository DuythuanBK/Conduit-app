package io.brandy.realworld.domain.user.repository;

import io.brandy.realworld.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.name = :name OR u.email = :email")
    Optional<UserEntity> findByNameOrEmail(String name, String email);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByName(String name);
}
