package com.luxlog.api.config.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSession {
    private static Long sequence = 1L;
    private String name;
    private Long id;

    public UserSession(String name) {
        this.name = name;
        id = sequence++;
    }
}
