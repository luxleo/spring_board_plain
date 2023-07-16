package com.luxlog.api.service;

import com.luxlog.api.domain.Post;
import com.luxlog.api.domain.PostEditor;
import com.luxlog.api.exception.PostNotFoundException;
import com.luxlog.api.repository.PostRepository;
import com.luxlog.api.request.PostCreate;
import com.luxlog.api.request.PostEdit;
import com.luxlog.api.request.PostSearch;
import com.luxlog.api.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("글 작성")
    void writePostTest() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목이오")
                .content("내용이오")
                .build();
        // when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals(post.getTitle(), "제목이오");
        assertEquals(post.getContent(), "내용이오");
    }

    @Test
    @DisplayName("글 1개 조회")
    void findOne() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        //when
        PostResponse postResponse = postService.get(requestPost.getId());
        //then
        assertNotNull(postResponse);
        assertEquals("foo", postResponse.getTitle());
        assertEquals("bar", postResponse.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getList() {
        //given
        List<Post> requestPosts = IntStream.range(1, 21)
                .mapToObj(i ->
                        Post.builder()
                                .title("test post" + i)
                                .content("test content" + i)
                                .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //when
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        List<Post> posts = postRepository.getList(postSearch);

        //then
        assertEquals(10L, posts.size()); // pageable로 지정한 수 만큼 repository.findAll()메서드에서 조회
        assertEquals("test content20", posts.get(0).getContent()); // Sort.By(order) DESC 지정해서
    }

    @Test
    @DisplayName("글 수정")
    void editTest() {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("반포자이")
                .build();
        postRepository.save(post);
        //when
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor editor = editorBuilder.title("dragon")
                .build();
        post.edit(editor);
        // then
        assertEquals("dragon", post.getTitle());
        assertEquals("반포자이", post.getContent());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editPost() {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post);
        //when
        PostEdit postEdit = PostEdit.builder()
                .title("dragon1")
                .build();
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
        //then
        Post editedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("no such post"));
        assertEquals("dragon1", editedPost.getTitle());
        assertEquals("bar1", post.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteTest() {
        //given
        Post post = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(post);
        assertEquals(1L, postRepository.count());
        //when
        postService.delete(post.getId());
        //then
        assertEquals(0L, postRepository.count());
    }

    @Test
    @DisplayName("없는 게시글 삭제 및 수정시 PostNotFoundException")
    void noSuchPost() {
        //then
        assertThrows(PostNotFoundException.class, () -> postService.delete(1L));
    }
}