package com.luxlog.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@ToString
public class PostEdit {
    @NotBlank(message = "수정하고자 하는 제목을 꼭 넣어주세요")
    private String title;
    @NotBlank(message = "수정하고자 하는 내용을 꼭 넣어주세요")
    private String content;
    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
