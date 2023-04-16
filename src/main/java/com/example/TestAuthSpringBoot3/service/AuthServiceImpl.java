package com.example.TestAuthSpringBoot3.service;

import com.example.TestAuthSpringBoot3.dto.CredentialsDTO;
import com.example.TestAuthSpringBoot3.dto.RegistrationDTO;
import com.example.TestAuthSpringBoot3.entity.Role;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.repository.service.UserRepositoryService;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryService userRepositoryService;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    private final AuthenticationProvider authenticationProvider;

    @Override
    public ResponseEntity<?> register(RegistrationDTO reg) {
        if (reg != null &&
                reg.getEmail() != null &&
                reg.getUsername() != null &&
                reg.getPassword() != null) {

            var user = createDefaultUser(reg);

            var username = reg.getUsername();

            try {
                userRepositoryService.getUserFromUsername(username);

                return new ResponseEntity<>(
                        "Cannot register user with username " + username + ", is already registered",
                        HttpStatus.UNAUTHORIZED);

            } catch (UsernameNotFoundException ex) {
                log.info("Username " + username + " not found. Save it in database");
                userRepositoryService.saveUser(user);
            }

            return ResponseEntity.ok("Successfully registered");
        }
        return new ResponseEntity<>("Null body", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> login(CredentialsDTO cred) {
        if (cred != null) {
            var username = cred.getUsername();
            var password = cred.getPassword();

            var userDetails = User.builder().username(username).password(password).build();
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());

            try {
                var successAuthentication = authenticationProvider.authenticate(authentication);
                return ResponseEntity.ok(jwtService.generateTokenPair(successAuthentication));
            } catch (AuthenticationException ex) {
                return new ResponseEntity<>(
                        "Provided credentials cannot be authenticated ", HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>("Null body", HttpStatus.BAD_REQUEST);
    }

    private User createDefaultUser(RegistrationDTO reg) {
        var user = User.builder()
                .email(reg.getEmail())
                .username(reg.getUsername())
                .password(encoder.encode(reg.getPassword()))
                .accountEnabled(true)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();

        var roleDefault = Role.builder()
                .user(user)
                .role("ROLE_DEFAULT")
                .expirationDate(new Date(0)) // Without expiration date
                .build();

        var roleUser = Role.builder()
                .user(user)
                .role("ROLE_USER")
                .expirationDate(new Date(0)) // Without expiration date
                .build();

        var roles = Arrays.asList(roleDefault, roleUser);
        user.setRoles(roles);

        return user;
    }

}
