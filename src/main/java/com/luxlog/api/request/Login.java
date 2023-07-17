package com.luxlog.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
public class Login {
    @NotBlank(message = "아이디 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호 입력해주세요")
    private String password;
}
