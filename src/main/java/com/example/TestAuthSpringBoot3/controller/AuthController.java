package com.example.TestAuthSpringBoot3.controller;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<?> test() {
        User user = new User();
        user.setUsername("Eder");
        user.setPassword("12345");

        Authentication authentication = UsernamePasswordAuthenticationToken
                .authenticated(user, "12345", Collections.emptyList());


        TokenDTO tokenDTO = jwtService.generateTokenPair(authentication);
        log.error("Access token: " + tokenDTO.getAccessToken());
        log.error("Refresh token: " + tokenDTO.getRefreshToken());

        String username = jwtService.extractUsername(tokenDTO.getAccessToken());
        log.error("Username: " + username);

        return ResponseEntity.ok("Test OK");
    }
}
