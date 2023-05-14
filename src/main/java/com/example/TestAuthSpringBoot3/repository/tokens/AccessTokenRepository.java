package com.example.TestAuthSpringBoot3.repository.tokens;

import com.example.TestAuthSpringBoot3.entity.AccessToken;
import com.example.TestAuthSpringBoot3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

    @Modifying
    @Query("UPDATE AccessToken at SET at.valid = false WHERE at.user = ?1")
    void invalidateTokenByUser(User user);

    @Query("SELECT at FROM AccessToken at WHERE at.user = ?1 AND at.valid = true")
    Optional<AccessToken> getValidTokenByUser(User user);

}
