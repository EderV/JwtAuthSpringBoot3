package com.example.TestAuthSpringBoot3.repository.tokens.service;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.exception.TokenNotFoundException;

public interface TokensRepositoryService {

    void saveAccessToken(AccessToken accessToken);

    void saveRefreshToken(RefreshToken refreshToken);

    void invalidateAccessToken(User user);

    void invalidateRefreshToken(User user);

    TokenDTO findValidTokenTokenPairByUser(User user) throws TokenNotFoundException;

}
