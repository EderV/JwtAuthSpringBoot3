package com.example.TestAuthSpringBoot3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private Long id;

    @Override
    public String getAuthority() {
        return null;
    }

}
