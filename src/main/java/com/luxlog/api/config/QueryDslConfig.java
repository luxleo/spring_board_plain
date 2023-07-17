package com.luxlog.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {
    @PersistenceContext
    public EntityManager em;

    @Bean
    public JPAQueryFactory query() {
        return new JPAQueryFactory(em);
    }
}
