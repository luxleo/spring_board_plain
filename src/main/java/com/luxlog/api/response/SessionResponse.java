package com.luxlog.api.response;

import lombok.Data;

@Data
public class SessionResponse {
    private String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
