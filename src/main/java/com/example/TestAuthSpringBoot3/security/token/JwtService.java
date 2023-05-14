package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtService {

    TokenDTO getTokenPair(Authentication authentication);

    String extractUsernameFromAccessToken(String token);

    String extractUsernameFromRefreshToken(String token);

    boolean validateAccessToken(String token)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException;

    boolean validateRefreshToken(String token)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException;

    void invalidateTokens(String username);

    Date extractExpirationDate(String token);

}
