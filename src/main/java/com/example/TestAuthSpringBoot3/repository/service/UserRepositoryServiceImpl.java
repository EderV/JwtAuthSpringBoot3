package com.example.TestAuthSpringBoot3.repository.service;

import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {

    }

    @Override
    public User getUserFromUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.loadUserByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("User with username " + username + " not found");
    }
}
