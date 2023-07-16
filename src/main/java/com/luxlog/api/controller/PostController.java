package com.luxlog.api.controller;

import com.luxlog.api.request.PostCreate;
import com.luxlog.api.request.PostEdit;
import com.luxlog.api.request.PostSearch;
import com.luxlog.api.response.PostResponse;
import com.luxlog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 글 포스트 할때 받아준다(폼 데이터로 넘김)
     */
    @PostMapping("/posts")
    public String postArticle(@RequestBody @Valid PostCreate params) {
        log.info("params : {}", params.toString());
        params.validate();
        postService.write(params);
        return "Hello World";
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);

    }

    @GetMapping("/posts/{postId}")
    public PostResponse getOne(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
