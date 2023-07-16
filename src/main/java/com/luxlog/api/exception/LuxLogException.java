package com.luxlog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class LuxLogException extends RuntimeException{
    public final Map<String,String> validations = new HashMap<>();
    public LuxLogException(String message) {
        super(message);
    }

    public LuxLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatus();

    public void addValidation(String fieldName, String message) {
        validations.put(fieldName, message);
    }
}
