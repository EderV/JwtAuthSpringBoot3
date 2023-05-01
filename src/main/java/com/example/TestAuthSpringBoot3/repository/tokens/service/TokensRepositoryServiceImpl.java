package com.example.TestAuthSpringBoot3.repository.tokens.service;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;
import com.example.TestAuthSpringBoot3.repository.tokens.AccessTokenRepository;
import com.example.TestAuthSpringBoot3.repository.tokens.RefreshTokenRepository;
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

    @Override
    public void invalidateAccessToken(User user) {
        accessTokenRepository.invalidateTokenByUser(user);
    }

    @Override
    public void invalidateRefreshToken(User user) {
        refreshTokenRepository.invalidateTokenByUser(user);
    }

    @Override
    public TokenDTO findValidTokenTokenPairByUser(User user) throws TokenNotFoundException {
        var accessTokenOptional = accessTokenRepository.getValidTokenByUser(user);
        var refreshTokenOptional = refreshTokenRepository.getValidTokenByUser(user);

        if (accessTokenOptional.isPresent() && refreshTokenOptional.isPresent()) {
            return new TokenDTO(
                    user.getId(),
                    accessTokenOptional.get().getToken(),
                    refreshTokenOptional.get().getToken());
        }

        throw new TokenNotFoundException("Token pair for user: " + user.getUsername() + " not found");
    }

}