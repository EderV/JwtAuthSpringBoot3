package com.example.TestAuthSpringBoot3.security.filter;

import com.example.TestAuthSpringBoot3.repository.UserRepository;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(null);

//        if (!requestMatcher.matches(request)) {
            var authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                var jwt = authHeader.substring(7);
                var username = jwtService.extractUsername(jwt);
                var user = userRepository.loadUserByUsername(username);

                if (user != null) {
                    var authorities = new ArrayList<GrantedAuthority>();
                    authorities.add((GrantedAuthority) () -> "ROLE_ADMIN");

                    var authentication = UsernamePasswordAuthenticationToken
                            .authenticated(user, null, authorities);
                    log.error("Is authenticated: " + authentication.isAuthenticated());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
//        }

        filterChain.doFilter(request, response);
    }

}
