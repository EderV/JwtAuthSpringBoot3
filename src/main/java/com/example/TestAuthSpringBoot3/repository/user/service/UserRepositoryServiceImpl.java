package com.example.TestAuthSpringBoot3.repository.user.service;

import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserFromUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.loadUserByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("User with username " + username + " not found");
    }

    @Override
    public User getUserById(int id) throws UsernameNotFoundException {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("User with id " + id + " not found");
    }
}
