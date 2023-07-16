package com.luxlog.api.repository;

import com.luxlog.api.domain.Post;
import com.luxlog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
