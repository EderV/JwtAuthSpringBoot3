package com.example.TestAuthSpringBoot3.service;

import com.example.TestAuthSpringBoot3.dto.CredentialsDTO;
import com.example.TestAuthSpringBoot3.dto.RegistrationDTO;
import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import com.example.TestAuthSpringBoot3.entity.User;
import com.example.TestAuthSpringBoot3.repository.UserRepository;
import com.example.TestAuthSpringBoot3.security.token.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Override
    public ResponseEntity<?> register(RegistrationDTO reg) {
        if (reg != null) {
            User user = User.builder()
                    .username(reg.getUsername())
                    .password(encoder.encode(reg.getPassword()))
                    .build();

            if (userRepository.loadUserByUsername(reg.getUsername()) == null) {
                userRepository.save(user);
            }

            Authentication authentication = UsernamePasswordAuthenticationToken
                    .authenticated(user, null, Collections.emptyList());

            return ResponseEntity.ok(jwtService.generateTokenPair(authentication));
        }
        return new ResponseEntity<>("Null body", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> login(CredentialsDTO cred) {
        if (cred != null) {
            UserDetails user = userRepository.loadUserByUsername(cred.getUsername());
            if (user != null) {
                if (encoder.matches(cred.getPassword(), user.getPassword())) {
                    Authentication authentication = UsernamePasswordAuthenticationToken
                            .authenticated(user, null, Collections.emptyList());

                    return ResponseEntity.ok(jwtService.generateTokenPair(authentication));
                }
                else {
                    return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
                }
            }
            else {
                return new ResponseEntity<>("User does not exist", HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>("Null body", HttpStatus.BAD_REQUEST);
    }
}
