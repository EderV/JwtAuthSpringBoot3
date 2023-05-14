package com.example.TestAuthSpringBoot3.repository.tokens;

import com.example.TestAuthSpringBoot3.entity.RefreshToken;
import com.example.TestAuthSpringBoot3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.valid = false WHERE rt.user = ?1")
    void invalidateTokenByUser(User user);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user = ?1 AND rt.valid = true")
    Optional<RefreshToken> getValidTokenByUser(User user);

}
