package com.example.TestAuthSpringBoot3.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated())

                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()

                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                        .accessDeniedHandler(new AccessDeniedHandlerImpl()));

        return http.build();
    }

}
