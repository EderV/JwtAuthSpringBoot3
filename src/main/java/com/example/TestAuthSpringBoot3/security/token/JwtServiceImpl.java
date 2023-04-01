package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.security.keys.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final TokenGenerator tokenGenerator;
    private final KeyUtils keyUtils;

    @Override
    public TokenDTO generateTokenPair(Authentication authentication) {
        return tokenGenerator.createToken(authentication);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Date extractExpirationDate(String token) {
        return null;
    }

    private Claims extractClaims(String token) {
        var jwts = Jwts
                .parserBuilder()
                .setSigningKey(keyUtils.getAccessTokenPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return jwts;
    }

}
