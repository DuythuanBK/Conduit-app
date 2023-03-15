package io.brandy.realworld.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JWTAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf()
//                .disable()
//                .formLogin().disable()
//                .authorizeRequests()
//                .antMatchers("/users/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS ,"/**").permitAll()
//                .antMatchers(HttpMethod.GET ,"/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint
//                        (new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//    }

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .requestMatchers((matchers) -> {
                    matchers.antMatchers(HttpMethod.GET,"/api/articles/:slug","/**");
                    matchers.mvcMatchers("/api/users/**");
                })
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin()
                .disable()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowCredentials(true);
//        configuration.addAllowedOrigin("*");
//        configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));
//        configuration.addAllowedHeader("*");
//        configuration.setMaxAge((long)86400);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
