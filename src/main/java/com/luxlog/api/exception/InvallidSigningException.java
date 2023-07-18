package com.luxlog.api.exception;

public class InvallidSigningException extends LuxLogException {
    private static final String MESSAGE = "아이디/비밀번호가 일치하지 않습니다";
    public InvallidSigningException() {
        super(MESSAGE);
    }
    @Override
    public int getStatus() {
        return 400;
    }
}
