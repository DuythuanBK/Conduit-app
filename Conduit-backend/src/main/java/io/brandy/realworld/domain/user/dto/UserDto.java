package io.brandy.realworld.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserDto {
    private String email;
    private String token;

    @JsonProperty("username")
    private String name;
    private String bio;
    private String image;

    @Getter
    @Builder
    @AllArgsConstructor
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Registration {
        @JsonProperty("username")
        @NotNull
        @Pattern(regexp = "[\\w\\d]{1,30}", message = "String constains alphabet or digit with length 1 to 30")
        private String name;

        @NotNull
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, max = 32)
        private String password;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Login {
        @NotNull
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, max = 32)
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Update {
        private Long id;
        private String email;
        private String name;
        private String bio;
        private String image;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Auth {
        private Long id;
        private String email;
        private String name;
        private String bio;
        private String image;
    }
}
