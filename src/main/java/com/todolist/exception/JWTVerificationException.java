package com.todolist.exception;

public class JWTVerificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JWTVerificationException(String message) {
        super(message);
    }

    public JWTVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
