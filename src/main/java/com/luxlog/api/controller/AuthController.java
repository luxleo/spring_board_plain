package com.luxlog.api.controller;

import com.luxlog.api.repository.UserRepository;
import com.luxlog.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    @PostMapping("/auth/login")
    public void login(@RequestBody Login login) {
        log.info("login value : {}",login);

    }
}
