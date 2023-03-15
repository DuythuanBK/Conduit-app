package io.brandy.realworld.domain.user.service;

import io.brandy.realworld.domain.user.dto.UserDto;

public interface UserService {
    UserDto registration(UserDto.Registration registration);
    UserDto login(UserDto.Login login);
    UserDto currentUser(UserDto.Auth authUser);

    UserDto updateUser(UserDto.Auth authUser, UserDto.Update update);
}
