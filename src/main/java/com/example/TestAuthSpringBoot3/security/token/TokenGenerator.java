package com.example.TestAuthSpringBoot3.security.token;

import com.example.TestAuthSpringBoot3.dto.TokenDTO;
import org.springframework.security.core.Authentication;

public interface TokenGenerator {

    TokenDTO createToken(Authentication authentication);

}
