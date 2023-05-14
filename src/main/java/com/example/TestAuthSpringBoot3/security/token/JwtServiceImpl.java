package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;
import com.example.TestAuthSpringBoot3.repository.tokens.service.TokensRepositoryService;
import com.example.TestAuthSpringBoot3.repository.user.service.UserRepositoryService;
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

    private final UserRepositoryService userRepositoryService;
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
    public String extractUsernameFromAccessToken(String token) {
        return extractClaimsFromAccessToken(token).getSubject();
    }

    @Override
    public String extractUsernameFromRefreshToken(String token) {
        return extractClaimsFromRefreshToken(token).getSubject();
    }

    @Override
    public boolean validateAccessToken(String token) throws
            SignatureException,
            MalformedJwtException,
            ExpiredJwtException,
            UnsupportedJwtException,
            IllegalArgumentException,
            TokenNotFoundException {

        // If something of this went wrong, an exception is thrown. Token is invalid
        var username = extractUsernameFromAccessToken(token);
        var user = userRepositoryService.getUserFromUsername(username);
        var accessToken = tokensRepositoryService.findValidAccessTokenByUser(user);

        return token.equals(accessToken);
    }

    @Override
    public boolean validateRefreshToken(String token) throws
            SignatureException,
            MalformedJwtException,
            ExpiredJwtException,
            UnsupportedJwtException,
            IllegalArgumentException,
            TokenNotFoundException {

        var username = extractUsernameFromRefreshToken(token);
        var user = userRepositoryService.getUserFromUsername(username);
        var refreshToken = tokensRepositoryService.findValidRefreshTokenByUser(user);

        return token.equals(refreshToken);
    }

    @Override
    public void invalidateTokens(String username) {
        var user = userRepositoryService.getUserFromUsername(username);
        tokensRepositoryService.invalidateAccessToken(user);
        tokensRepositoryService.invalidateRefreshToken(user);
    }

    @Override
    public Date extractExpirationDate(String token) {
        return null;
    }

    private Claims extractClaimsFromAccessToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(keyUtils.getAccessTokenPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractClaimsFromRefreshToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(keyUtils.getRefreshTokenPublicKey())
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
