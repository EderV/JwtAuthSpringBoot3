package com.example.TestAuthSpringBoot3.service;

import com.example.TestAuthSpringBoot3.dto.CredentialsDTO;
import com.example.TestAuthSpringBoot3.dto.RegistrationDTO;
import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> register(RegistrationDTO reg);
    ResponseEntity<?> login(CredentialsDTO cred);

}
