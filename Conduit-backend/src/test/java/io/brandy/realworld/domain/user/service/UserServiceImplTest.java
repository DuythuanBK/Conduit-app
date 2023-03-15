package io.brandy.realworld.domain.user.service;

import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.entity.UserEntity;
import io.brandy.realworld.domain.user.repository.UserRepository;
import io.brandy.realworld.expection.AppException;
import io.brandy.realworld.expection.Error;
import io.brandy.realworld.security.JwtUtils;
import org.apache.catalina.User;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    private AutoCloseable autoCloseable;

    private final String email = "thuan@gmail.com";
    private final String name = "thuan";

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, jwtUtils);
    }

    @Test
    void registration() {
        UserDto.Registration registration = UserDto.Registration.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .password("1234")
                .build();

        UserDto userDto = userService.registration(registration);

        assertThat(userDto.getEmail()).isEqualTo(registration.getEmail());
        assertThat(userDto.getName()).isEqualTo(registration.getName());
    }

    @Test
    void loginSuccess() {
        UserDto.Login loginUser = UserDto.Login.builder()
                .email("thuan@gmail.com")
                .password("1234")
                .build();
        UserEntity userEntity = UserEntity.builder()
                                .email("thuan@gmail.com")
                                .name("thuan")
                                .build();

        given(userRepository.findByEmail(loginUser.getEmail()))
                .willReturn(Optional.of(userEntity));

        given(passwordEncoder.matches(loginUser.getPassword(), passwordEncoder.encode(loginUser.getPassword())))
                .willReturn(true);

        UserDto userDto = userService.login(loginUser);

    }

    @Test
    void loginFailedNotExistUser() {
        UserDto.Login loginUser = UserDto.Login.builder()
                .email("thuan@gmail.com")
                .password("1234")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .build();

        given(userRepository.findByEmail(loginUser.getEmail()))
                .willReturn(Optional.empty());

//        given(passwordEncoder.matches(loginUser.getPassword(), passwordEncoder.encode(loginUser.getPassword())))
//                .willReturn(true);

        assertThatThrownBy(() -> userService.login(loginUser))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(Error.USER_NOT_FOUND.getMessage());

    }

    @Test
    void loginFailedWrongPassword() {
        UserDto.Login loginUser = UserDto.Login.builder()
                .email("thuan@gmail.com")
                .password("1234")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .email("thuan@gmail.com")
                .name("thuan")
                .build();

        given(userRepository.findByEmail(loginUser.getEmail()))
                .willReturn(Optional.of(userEntity));

        given(passwordEncoder.matches(loginUser.getPassword(), passwordEncoder.encode(loginUser.getPassword())))
                .willReturn(false);

        assertThatThrownBy(() -> userService.login(loginUser))
                .isInstanceOf(AppException.class)
                .hasMessageContaining(Error.LOGIN_INFO_INVALID.getMessage());

    }

    @Test
    void currentUser() {
        UserEntity userEntity = UserEntity.builder()
                .id((long)1)
                .email(email)
                .name(name)
                .build();

        UserDto.Auth auth = UserDto.Auth.builder()
                .id((long)1)
                .email(email)
                .name(name)
                .build();

        given(userRepository.findById(auth.getId()))
                .willReturn(Optional.of(userEntity));

        UserDto userDto = userService.currentUser(auth);

        assertThat(userDto.getEmail()).isEqualTo(email);
    }

    @Test
    void updateUser() {
        UserEntity userEntity = UserEntity.builder()
                .id((long)1)
                .email(email)
                .name(name)
                .build();

        UserDto.Auth auth = UserDto.Auth.builder()
                .id((long)1)
                .email(email)
                .name(name)
                .build();

        UserDto.Update userUpdate = UserDto.Update.builder()
                .email(email)
                .name(name)
                .bio("Update bio")
                .image("update image")
                .password("new password")
                .build();

        given(userRepository.findById(auth.getId()))
                .willReturn(Optional.of(userEntity));


        UserDto userDto = userService.updateUser(auth, userUpdate);

        assertThat(userDto.getEmail()).isEqualTo(email);

    }

}