package com.luxlog.api.response;

import com.luxlog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

/**
 * response에 필요한 로직을 처리하기 위한 객체
 * constructor에 커스텀 로직 필요한 필드 재정의
 */
@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post) {
        id = post.getId();
        title = editedTitle(post.getTitle());
        content = post.getContent();
    }

    /**
     * 제목 길이 열글자 로만 리턴하도록 한다.
     */
    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(),10));
        this.content = content;
    }

    public static String editedTitle(String title) {
        return title.substring(0, Math.min(10, title.length()));
    }
}
