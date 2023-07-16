package com.luxlog.api.exception;

public class PostNotFoundException extends LuxLogException{
    private static final String MESSAGE = "해당 게시글은 존재하지 않습니다.";

    public PostNotFoundException(Throwable cause) {
        super(MESSAGE,cause);
    }

    public PostNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}
