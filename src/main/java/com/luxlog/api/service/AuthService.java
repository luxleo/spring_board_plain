package com.luxlog.api.service;

import com.luxlog.api.domain.Session;
import com.luxlog.api.domain.User;
import com.luxlog.api.exception.InvallidSigningException;
import com.luxlog.api.repository.UserRepository;
import com.luxlog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    @Transactional
    public String signInSession(Login request) {
        User user = repository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvallidSigningException::new);
        Session session = Session.builder()
                .user(user)
                .build();
        user.addSession(session);
        return session.getAccessToken();
    }
    @Transactional
    public Long signIn(Login request) {
        User user = repository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvallidSigningException::new);
        return user.getId();
    }
}
