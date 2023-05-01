package com.example.TestAuthSpringBoot3.security.config;

import com.example.TestAuthSpringBoot3.security.error.AdvancedAuthenticationEntryPoint;
import com.example.TestAuthSpringBoot3.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/role").hasRole("ADMIN")
                .anyRequest().authenticated())

                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()

                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new AdvancedAuthenticationEntryPoint())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl()));

        return http.build();
    }

}
