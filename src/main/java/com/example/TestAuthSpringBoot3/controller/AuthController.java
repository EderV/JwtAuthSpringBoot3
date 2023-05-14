package com.example.TestAuthSpringBoot3.controller;

import com.example.TestAuthSpringBoot3.dto.CredentialsDTO;
import com.example.TestAuthSpringBoot3.dto.RegistrationDTO;
import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import com.example.TestAuthSpringBoot3.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDTO reg) {
        return authService.register(reg);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsDTO cred) {
        return authService.login(cred);
    }

    @PostMapping("/token")
    public ResponseEntity<?> updateToken(@RequestBody TokenDTO tokenDto) {
        return authService.updateTokens(tokenDto);
    }

}
