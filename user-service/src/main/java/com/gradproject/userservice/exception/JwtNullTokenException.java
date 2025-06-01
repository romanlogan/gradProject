package com.gradproject.userservice.exception;

public class JwtNullTokenException extends RuntimeException {
    public JwtNullTokenException(String message){
        super(message);
    }
}
