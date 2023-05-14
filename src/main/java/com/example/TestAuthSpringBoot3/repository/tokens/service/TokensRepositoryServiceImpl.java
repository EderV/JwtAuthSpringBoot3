package com.example.TestAuthSpringBoot3.repository.tokens.service;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;
import com.example.TestAuthSpringBoot3.repository.tokens.AccessTokenRepository;
import com.example.TestAuthSpringBoot3.repository.tokens.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokensRepositoryServiceImpl implements TokensRepositoryService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveAccessToken(AccessToken accessToken) {
        accessTokenRepository.save(accessToken);
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    @Override
    public void invalidateAccessToken(User user) {
        accessTokenRepository.invalidateTokenByUser(user);
    }

    @Transactional
    @Override
    public void invalidateRefreshToken(User user) {
        refreshTokenRepository.invalidateTokenByUser(user);
    }

    @Override
    public String findValidAccessTokenByUser(User user) throws TokenNotFoundException {
        var accessTokenOptional = accessTokenRepository.getValidTokenByUser(user);

        if (accessTokenOptional.isPresent()) {
            return accessTokenOptional.get().getToken();
        }

        throw new TokenNotFoundException("Access token for user: " + user.getUsername() + " not found");
    }

    @Override
    public String findValidRefreshTokenByUser(User user) throws TokenNotFoundException {
        var refreshTokenOptional = refreshTokenRepository.getValidTokenByUser(user);

        if (refreshTokenOptional.isPresent()) {
            return refreshTokenOptional.get().getToken();
        }

        throw new TokenNotFoundException("Refresh token for user: " + user.getUsername() + " not found");
    }

    @Override
    public TokenDTO findValidTokenTokenPairByUser(User user) throws TokenNotFoundException {
        var accessToken = findValidAccessTokenByUser(user);
        var refreshToken = findValidRefreshTokenByUser(user);

        return new TokenDTO(user.getId(), accessToken, refreshToken);
    }

}