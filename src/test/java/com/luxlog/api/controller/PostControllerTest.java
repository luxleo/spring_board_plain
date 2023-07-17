package com.luxlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxlog.api.domain.Post;
import com.luxlog.api.repository.PostRepository;
import com.luxlog.api.request.PostCreate;
import com.luxlog.api.request.PostEdit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("/posts 시 title은 비어있으면 에러를 던진다.")
    void validationTest() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용만 있구려")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청이오"))
                .andExpect(jsonPath("$.validation.title").value("제목은 비어있을수 없소"))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts service.save를 하면 db에 저장이된다..")
    void saveTest() throws Exception {
        //given
        PostCreate req = PostCreate.builder()
                .title("dragon")
                .content("단순 내용이오")
                .build();
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        // when
        assertThat(postRepository.count()).isEqualTo(1);

        // then
        Post post = postRepository.findAll().get(0);
        assertEquals("dragon", post.getTitle());
        assertEquals("단순 내용이오", post.getContent());
    }

    @Test
    @DisplayName("포스트 단 건 조회")
    void findOnePostById() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("일이삼사오육칠팔구십십일")
                .content("bar")
                .build();
        postRepository.save(post1);
        //expect : 제목은 10글자로만 제한둬서 json에 담아주자
        mockMvc.perform(get("/posts/{postId}", post1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("일이삼사오육칠팔구십"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 여러건 조회")
    void listTest() throws Exception {
        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .content("content " + i)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(posts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].title").value("title 30"))
                .andExpect(jsonPath("$[0].content").value("content 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editPost() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post);
        //when
        PostEdit postEdit = PostEdit.builder()
                .title("dragon1")
                .content("bar1")
                .build();

        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post);

        //expect
        mockMvc.perform(delete("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("없는 게시글 수정 및 삭제시 404응답")
    void noSuchPost() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("foo1")
                .content("bar1")
                .build();
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(print());
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("금지어 \'바보\'가 포함되면 InvalidException")
    void invalidContent() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("foo1")
                .content("바보다.")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);
        //expect
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}