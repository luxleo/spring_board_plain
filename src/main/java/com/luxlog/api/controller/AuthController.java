package com.luxlog.api.controller;

import com.luxlog.api.request.Login;
import com.luxlog.api.response.SessionResponse;
import com.luxlog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    /**
     * 로그인 성공 시 accessToken반환 해준다
     */
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        log.info("login value : {}",login);
        String accessToken = service.signIn(login);
        //쿠키에 세션 넣는 전통적 방
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") //TODO: 운영환경에 따른 값 설정 필
                .path("/")
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        return new SessionResponse(accessToken);
    }
}
