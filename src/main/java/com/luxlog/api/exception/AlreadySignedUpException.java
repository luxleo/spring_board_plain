package com.luxlog.api.exception;

public class AlreadySignedUpException extends LuxLogException {
    private static final String MESSAGE = "이미 가입하셨소만...";
    public AlreadySignedUpException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
