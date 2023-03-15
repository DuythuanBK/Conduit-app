package io.brandy.realworld.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
public class JwtUtilsConfiguration {
    @Bean
    public JwtUtils getJwtUtils(@Value("${realworld.auth.token.sign-key}") String signKey,
                                @Value("${realworld.auth.token.valid-time}") Long validTime ) throws Exception {
        if (signKey.length() < 32) {
            throw new Exception("SignKey must have length at least 32");
        }

        return new JwtUtils(signKey, validTime);
    }
}
