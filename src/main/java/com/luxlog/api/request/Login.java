package com.luxlog.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Login {
    @NotBlank(message = "아이디 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호 입력해주세요")
    private String password;

    @Builder
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
