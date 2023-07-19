package com.luxlog.api.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUp {
    private String name;
    private String password;
    private String email;
    @Builder
    public SignUp(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
