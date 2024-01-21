package com.example.TestAuthSpringBoot3.security.filter;

import com.example.TestAuthSpringBoot3.exception.CustomAuthenticationException;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${endpoints.public}")
    private String publicEndpoint;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        var publicEndpointMatcher = createPublicEndpointMatcher();
        if (!publicEndpointMatcher.matches(request)) {
            try {
                var jwt = extractJwtFromHeader(request);

                if (isJwtValid(jwt)) {
                    var user = loadUserFromRepository(jwt);
                    setAuthenticationInSecurityContext(user, request);
                }
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private RequestMatcher createPublicEndpointMatcher() {
        return new AntPathRequestMatcher(publicEndpoint);
    }

    private String extractJwtFromHeader(HttpServletRequest request) throws AuthenticationException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new CustomAuthenticationException("Incorrect authorization header");
    }

    private boolean isJwtValid(String jwt) throws AuthenticationException {
        try {
            return jwtService.validateAccessToken(jwt);
        } catch (SignatureException e) {
            throw new CustomAuthenticationException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new CustomAuthenticationException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new CustomAuthenticationException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new CustomAuthenticationException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CustomAuthenticationException("JWT claims string is empty: " + e.getMessage());
        } catch (TokenNotFoundException e) {
            throw new CustomAuthenticationException("Given token do not match with the tokens belonging to the user: " + e.getMessage());
        }
    }

    private UserDetails loadUserFromRepository(String jwt) throws AuthenticationException {
        try {
            var username = jwtService.extractUsernameFromAccessToken(jwt);
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new CustomAuthenticationException("Username extracted from JWT not found in the database");
        }
    }

    private void setAuthenticationInSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        var authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
