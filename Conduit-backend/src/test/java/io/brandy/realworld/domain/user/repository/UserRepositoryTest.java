package io.brandy.realworld.domain.user.repository;

import io.brandy.realworld.domain.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void WhenNameOrEmailExist() {
        UserEntity userEntity = UserEntity.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .password("1234")
                .build();

        userRepository.save(userEntity);

        // pass email and name
        Optional<UserEntity> user = userRepository.findByNameOrEmail(userEntity.getName(), userEntity.getEmail());

        assertThat(user.isEmpty()).isFalse();

        // pass email, name =""
        user = userRepository.findByNameOrEmail("", userEntity.getEmail());

        assertThat(user.isEmpty()).isFalse();

        // pass name
        user = userRepository.findByNameOrEmail(userEntity.getName(), "");

        assertThat(user.isEmpty()).isFalse();

        // pass name and email are not exist in database
        user = userRepository.findByNameOrEmail("khanh", "khanh@gmail.com");

        assertThat(user.isEmpty()).isTrue();

    }

    @Test
    void WhenEmailExists() {
        UserEntity userEntity = UserEntity.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .password("1234")
                .build();
        userRepository.save(userEntity);

        Optional<UserEntity> user = userRepository.findByEmail(userEntity.getEmail());

        assertThat(user.isEmpty()).isFalse();

    }

    @Test
    void WhenEmailNotExists() {

        Optional<UserEntity> user = userRepository.findByEmail("khanh@gmail.com");

        assertThat(user.isEmpty()).isTrue();

    }

    @Test
    void WhenNameExists() {
        UserEntity userEntity = UserEntity.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .password("1234")
                .build();
        userRepository.save(userEntity);

        Optional<UserEntity> user = userRepository.findByName(userEntity.getName());

        assertThat(user.isEmpty()).isFalse();
    }

    @Test
    void WhenNameNotExists() {

        Optional<UserEntity> user = userRepository.findByName("NoName");

        assertThat(user.isEmpty()).isTrue();
    }
}