package io.brandy.realworld.domain.user.controller;

import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin
public class UsersController {
    private final UserService userService;

    @PostMapping
    public UserDto registration(@RequestBody @Valid UserDto.Registration registration) {

        return userService.registration(registration);

    }

    @PostMapping("/login")
    public UserDto login(@RequestBody @Valid UserDto.Login login) {

        return userService.login(login);

    }
}
