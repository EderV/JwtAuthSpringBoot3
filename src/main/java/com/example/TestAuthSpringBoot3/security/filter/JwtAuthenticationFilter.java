package com.example.TestAuthSpringBoot3.security.filter;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var publicEndpointMatcher = createPublicEndpointMatcher();
        if (!publicEndpointMatcher.matches(request)) {
            var jwt = extractJwtFromHeader(request);

            if (isJwtValid(jwt)) {
                var user = loadUserFromRepository(jwt);
                setAuthenticationInSecurityContext(user, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private RequestMatcher createPublicEndpointMatcher() {
        return new AntPathRequestMatcher(publicEndpoint);
    }

    private String extractJwtFromHeader(HttpServletRequest request) throws ServletException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new ServletException("Incorrect authorization header");
    }

    private boolean isJwtValid(String jwt) throws ServletException {
        try {
            jwtService.validateToken(jwt);
            return true;
        } catch (SignatureException e) {
            throw new ServletException("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new ServletException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ServletException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new ServletException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ServletException("JWT claims string is empty: " + e.getMessage());
        }
    }

    private UserDetails loadUserFromRepository(String jwt) throws ServletException {
        try {
            var username = jwtService.extractUsername(jwt);
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new ServletException("Username extracted from JWT not found in the database");
        }
    }

    private void setAuthenticationInSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        var authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
