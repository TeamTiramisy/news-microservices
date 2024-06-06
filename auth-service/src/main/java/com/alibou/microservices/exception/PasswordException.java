package com.alibou.microservices.exception;

public class PasswordException extends RuntimeException {

    public PasswordException(String message) {
        super(message);
    }
}
