package com.luxlog.api.exception;

public class UnauthorizedException extends LuxLogException {
    private static final String MESSAGE = "유효하지 않은 사용자입니다.";
    public UnauthorizedException() {
        super(MESSAGE);
    }

    public UnauthorizedException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * http 응답중 unauthorized는 401이다.
     */
    @Override
    public int getStatus() {
        return 401;
    }
}
