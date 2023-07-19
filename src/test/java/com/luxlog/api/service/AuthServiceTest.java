package com.luxlog.api.service;

import com.luxlog.api.crypto.PasswordEncoder;
import com.luxlog.api.domain.User;
import com.luxlog.api.exception.AlreadySignedUpException;
import com.luxlog.api.repository.UserRepository;
import com.luxlog.api.request.Login;
import com.luxlog.api.request.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 테스트 시에는 @Profile("test")인 PlainPasswordEncoder를 구현체로 사용한다.
 */
@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() {
        //given
        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .name("test")
                .password("1234")
                .build();
        //when
        authService.signUp(signUp);
        //then
        assertThat(userRepository.count())
                .isEqualTo(1);
        User newUser = userRepository.findAll().iterator().next();
        assertThat(newUser.getEmail())
                .isEqualTo("test@gmail.com");
        assertThat(newUser.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("회원가입 중복시 실패")
    void invalidSignUp() {
        //given
        User user = User.builder()
                .email("test@gmail.com")
                .name("test")
                .password("1234")
                .build();
        userRepository.save(user);

        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .name("test")
                .password("1234")
                .build();
        //when
        //then
        assertThatThrownBy(() -> authService.signUp(signUp))
                .isInstanceOf(AlreadySignedUpException.class);
    }

    @Test
    @DisplayName("로그인 성공 with 암호화된 password")
    void loginSuccess() {
        //given
        SignUp signUp = SignUp.builder()
                .email("test@gmail.com")
                .password("1234")
                .name("test1")
                .build();

        authService.signUp(signUp);

        Login login = Login.builder()
                .email(signUp.getEmail())
                .password("1234")
                .build();
        //when
        Long id = authService.signIn(login);
        assertThat(userRepository.findById(id)).isNotNull();

        //then
    }
}