package com.example.TestAuthSpringBoot3.repository.user;

import com.example.TestAuthSpringBoot3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    @Query("SELECT u FROM User u LEFT JOIN Role r ON u.id = r.user.id WHERE u.username = ?1")
    @Query("" +
            "SELECT u FROM User u " +
            "LEFT JOIN FETCH u.roles r " +
            "WHERE u.username = ?1" +
            "")
    Optional<User> loadUserByUsername(String username);

    /*
        "LEFT JOIN FETCH u.roles r " +
        "LEFT JOIN u.accessTokens act " +
        "LEFT JOIN u.refreshTokens rft " +

     */

    /* AND act.valid = true AND rft.valid = true */

}
