package com.luxlog.api.config;

import com.luxlog.api.repository.PostRepository;
import com.luxlog.api.repository.PostRepositoryCustom;
import com.luxlog.api.repository.PostRepositoryImpl;
import com.luxlog.api.service.PostService;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

//@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {
    private final EntityManager em;

    //@Bean
    public PostService postService() {
        return new PostService((PostRepository) postRepository());
    }
    //@Bean
    public PostRepositoryCustom postRepository() {
        return new PostRepositoryImpl(em);
    }
}
