package com.luxlog.api.repository;

import com.luxlog.api.domain.Post;
import com.luxlog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.luxlog.api.domain.QPost.post;

@Repository
@Transactional
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return query.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffSet())
                .orderBy(post.id.desc())
                .fetch();
    }
}
