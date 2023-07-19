package com.luxlog.api.service;

import com.luxlog.api.crypto.PasswordEncoder;
import com.luxlog.api.domain.Session;
import com.luxlog.api.domain.User;
import com.luxlog.api.exception.AlreadySignedUpException;
import com.luxlog.api.exception.InvallidSigningException;
import com.luxlog.api.repository.UserRepository;
import com.luxlog.api.request.Login;
import com.luxlog.api.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
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
    public Long signInJwt(Login request) {
        User user = repository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvallidSigningException::new);
        return user.getId();
    }

    /**
     * password암호화 적용
     */
    @Transactional
    public Long signIn(Login request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(InvallidSigningException::new);

        boolean matches = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if (!matches) {
            throw new InvallidSigningException();
        }
        return user.getId();
        }

    /**
     * 회원가입 전에 이메일 중복 체크하고 진행한다.
     */
    public void signUp(SignUp signUp) {
        Optional<User> optionalUser = repository.findByEmail(signUp.getEmail());
        if (optionalUser.isPresent()) {
            throw new AlreadySignedUpException();
        }

        var user = User.builder()
                .name(signUp.getName())
                .password(passwordEncoder.encrypt(signUp.getPassword()))
                .email(signUp.getEmail())
                .build();
        repository.save(user);
    }
}
