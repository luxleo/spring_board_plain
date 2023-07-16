package com.luxlog.api.request;

import com.luxlog.api.exception.InvalidException;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter @Getter
@ToString
@NoArgsConstructor
public class PostCreate {
    @NotBlank(message = "제목은 비어있을수 없소")
    private String title;
    @NotBlank
    private String content;

    @Builder // builder 패턴 학습하자. -> 생성자의 변수명으로 주입한다. 나중에 호출할때는 상관 없나?
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (content.contains("바보")) {
            throw new InvalidException("title","제목에 바보라하면 안돼요!!");
        }
    }
}
