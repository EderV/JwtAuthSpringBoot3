package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtService {

    TokenDTO generateTokenPair(Authentication authentication);

    String extractUsername(String token);

    Date extractExpirationDate(String token);

}
