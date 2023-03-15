package io.brandy.realworld.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends GenericFilter {
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain
    ) throws IOException, ServletException {
        String jwt = jwtUtils.resolveToken(servletRequest);
        if(jwt != null && jwtUtils.validateToken(jwt)) {
            String username = jwtUtils.getSub(jwt);
            Authentication authentication = authenticationProvider.getAuthentication(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
