package io.brandy.realworld.domain.user.controller;

import io.brandy.realworld.domain.user.dto.UserDto;
import io.brandy.realworld.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto currentUser(@AuthenticationPrincipal UserDto.Auth authUser) {
        return userService.currentUser(authUser);
    }

    @PutMapping
    public UserDto updateUser(@AuthenticationPrincipal UserDto.Auth authUser, @Valid @RequestBody UserDto.Update update) {
        return userService.updateUser(authUser, update);
    }
}
