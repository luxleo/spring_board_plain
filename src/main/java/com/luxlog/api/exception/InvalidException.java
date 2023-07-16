package com.luxlog.api.exception;

import java.util.HashMap;
import java.util.Map;

public class InvalidException extends LuxLogException{
    private static final String MESSAGE = "유효하지 않소";
    public Map<String, String> validations = new HashMap<>();
    public InvalidException() {
        super(MESSAGE);
    }

    public InvalidException(Throwable cause) {
        super(MESSAGE);
    }

    public InvalidException(String fieldName,String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
