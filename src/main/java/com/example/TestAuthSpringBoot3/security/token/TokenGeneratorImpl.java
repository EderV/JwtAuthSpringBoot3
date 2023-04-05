package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.security.key.KeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class TokenGeneratorImpl implements TokenGenerator {

    private final KeyUtils keyUtils;

    @Override
    public TokenDTO createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("Principal {0} is not of User type", authentication.getPrincipal())
            );
        }

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));

//        String refreshToken;
//        if (authentication.getCredentials() instanceof Jwt jwt) {
//            Instant now = Instant.now();
//            Instant expiresAt = jwt.getExpiresAt();
//            Duration duration = Duration.between(now, expiresAt);
//            long daysUntilExpired = duration.toDays();
//            if (daysUntilExpired < 7) {
//                refreshToken = createRefreshToken(authentication);
//            }
//            else {
//                refreshToken = jwt.getTokenValue();
//            }
//        }
//        else {
//            refreshToken = createRefreshToken(authentication);
//        }

        tokenDTO.setRefreshToken(createRefreshToken(authentication));

        return tokenDTO;
    }
//
    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expirationDate = now.plus(5, ChronoUnit.MINUTES);

        String username = user.getUsername();

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationDate))
                .signWith(keyUtils.getAccessTokenPrivateKey())
                .compact();
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expirationDate = now.plus(30, ChronoUnit.DAYS);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationDate))
                .signWith(keyUtils.getRefreshTokenPrivateKey())
                .compact();
    }

}
