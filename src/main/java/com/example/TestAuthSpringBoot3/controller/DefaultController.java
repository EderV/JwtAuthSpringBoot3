package com.example.TestAuthSpringBoot3.controller;

import com.example.TestAuthSpringBoot3.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DefaultController {

    @GetMapping("/test")
    public ResponseEntity<?> test(@AuthenticationPrincipal User user) {
        log.error(user.toString());
        return ResponseEntity.ok("Hello " + user.getUsername());
    }

    @GetMapping("/test/role")
    public ResponseEntity<?> test2(@AuthenticationPrincipal User user) {
        log.error(user.toString());
        return ResponseEntity.ok("Test OK");
    }

}
