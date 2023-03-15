package io.brandy.realworld.domain.profile.controller;

import io.brandy.realworld.domain.profile.dto.ProfileDto;
import io.brandy.realworld.domain.profile.service.ProfileService;
import io.brandy.realworld.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@CrossOrigin
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileDto getProfile(@PathVariable("username") String username,@AuthenticationPrincipal UserDto.Auth authUser) {
        return profileService.getProfile(username, authUser);
    }

    @PostMapping("/{username}/follow")
    public ProfileDto followUser(@PathVariable("username") String username, @AuthenticationPrincipal UserDto.Auth authUser) {
        return profileService.followUser(username, authUser);
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto unfollowUser(@PathVariable("username") String username, @AuthenticationPrincipal UserDto.Auth authUser) {
        return profileService.unfollowUser(username, authUser);
    }

}
