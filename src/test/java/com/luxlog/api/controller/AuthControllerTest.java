package com.luxlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxlog.api.domain.Session;
import com.luxlog.api.domain.User;
import com.luxlog.api.repository.UserRepository;
import com.luxlog.api.request.Login;
import com.luxlog.api.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    AuthService service;
    @Autowired
    UserRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("초기 유저들이 있는지")
    void isDefaultUsers() {
        assertEquals(3L, repository.count());
    }

    @Test
    @DisplayName("기존에 생성된 유저 올바로 로그인시 Session 생성")
    void testLogin() throws Exception {

        //given 새로가입한 회원이있고
        User user1 = User.builder().name("test1")
                .email("test1@gmail.com")
                .password("1234")
                .build();
        repository.save(user1);

        Login login = Login.builder()
                .email("test1@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);
        Assertions.assertEquals(1, repository.count());

        //expect 로그인 성공시
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then 세션이 하나 만들어진다.
        Assertions.assertEquals(1L, user1.getSessions().size());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다")
    void accessTokenRequirePage() throws Exception {
        //given
        User user1 = User.builder().name("test1")
                .email("test1@gmail.com")
                .password("1234")
                .build();
        Session session = Session.builder()
                .user(user1)
                .build();
        user1.addSession(session);
        repository.save(user1);
        //expected
        mockMvc.perform(get("/foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", session.getAccessToken())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 후 유효하지 않은 세션 토큰 값으로 인증이 필요한 페이지에 접속불가하다.")
    void invalidAccessTokenRequirePage() throws Exception {
        //given
        User user1 = User.builder().name("test1")
                .email("test1@gmail.com")
                .password("1234")
                .build();
        Session session = Session.builder()
                .user(user1)
                .build();
        user1.addSession(session);
        repository.save(user1);
        String corruptedToken = session.getAccessToken()+"currupted";
        //expected
        mockMvc.perform(get("/foo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", corruptedToken)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


}