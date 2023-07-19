package com.luxlog.api.crypto;

public interface PasswordEncoder {
    public String encrypt(String rawPassword);

    public boolean matches(String rawPassword, String encodedPassword);
}
