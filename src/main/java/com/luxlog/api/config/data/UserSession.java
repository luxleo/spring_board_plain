package com.luxlog.api.config.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSession {
    private final Long id;

    public UserSession(Long id) {
        this.id = id;
    }
}
