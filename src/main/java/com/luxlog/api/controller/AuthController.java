package com.luxlog.api.controller;

import com.luxlog.api.request.Login;
import com.luxlog.api.response.SessionResponse;
import com.luxlog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final static String JWT_PRIVATE_KEY = "cd7+/e8dlsaOiRldLN5KCqrojWXKXnBr4KeFDq9ab/s=";
    /**
     * 로그인 성공 시 accessToken반환 해준다 session-cookie로그인
     */
    @PostMapping("/auth/login/cookie_session")
    public ResponseEntity login(@RequestBody Login login) {
        log.info("login value : {}", login);
        String accessToken = service.signInSession(login);
        //쿠키에 세션 넣는 전통적 방
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") //TODO: 운영환경에 따른 값 설정 필
                .path("/")
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    /**
     * jwt로그인 방식, 일회성 키를 사용하지 않고, 생성된 키를 저장한다.
     * 저장된 키를 이용하여 userId(subject)를 암호화한다.
     * 이 암호화 된 정보를 jwt라한다.
     * jwt를 AccessToken으롤 내려준다.
     * 이후 유저는 Authorization 리퀘스트 헤더필드에 jwt를 끼워 보낸다.
     * userId->jwt->accessToken
     */
    @PostMapping("/auth/login")
    public SessionResponse jwtLogin(@RequestBody Login login) {
        Long userId = service.signIn(login);
        // 일회성 key 발급 받아서 사용한다. => JWT_PRIVATE_KEY
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        byte[] encodedKey = key.getEncoded();
//        String strKey = Base64.getEncoder().encodeToString(encodedKey);

        byte[] decoded = Base64.getDecoder().decode(JWT_PRIVATE_KEY);
        SecretKey key = Keys.hmacShaKeyFor(decoded);
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .compact();

        return new SessionResponse(jws);
    }

}
