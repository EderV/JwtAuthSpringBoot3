package com.example.TestAuthSpringBoot3.repository.service;

import com.example.TestAuthSpringBoot3.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserRepositoryService {

    void saveUser(User user);

    User getUserFromUsername(String username) throws UsernameNotFoundException;

}
