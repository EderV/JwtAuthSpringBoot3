package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;
import com.example.TestAuthSpringBoot3.repository.tokens.service.TokensRepositoryService;
import com.example.TestAuthSpringBoot3.security.key.KeyUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final TokensRepositoryService tokensRepositoryService;
    private final TokenGenerator tokenGenerator;
    private final KeyUtils keyUtils;

    @Override
    public TokenDTO getTokenPair(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        try {
            return tokensRepositoryService.findValidTokenTokenPairByUser(user);
        } catch (TokenNotFoundException ex) {
            var tokenDto =  tokenGenerator.createToken(authentication);

            var accessToken = createAccessTokenObject(user, tokenDto.getAccessToken());
            var refreshToken = createRefreshTokenObject(user, tokenDto.getRefreshToken());
            saveAccessRefreshToken(accessToken, refreshToken);

            return tokenDto;
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public boolean validateToken(String token)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {

        Jwts.parserBuilder()
                .setSigningKey(keyUtils.getAccessTokenPublicKey())
                .build()
                .parseClaimsJws(token);

        return true;
    }

    @Override
    public Date extractExpirationDate(String token) {
        return null;
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(keyUtils.getAccessTokenPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private AccessToken createAccessTokenObject(User user, String token) {
        return AccessToken.builder()
                .user(user)
                .token(token)
                .valid(true)
                .build();
    }

    private RefreshToken createRefreshTokenObject(User user, String token) {
        return RefreshToken.builder()
                .user(user)
                .token(token)
                .valid(true)
                .build();
    }

    private void saveAccessRefreshToken(AccessToken accessToken, RefreshToken refreshToken) {
        tokensRepositoryService.saveAccessToken(accessToken);
        tokensRepositoryService.saveRefreshToken(refreshToken);
    }

}
