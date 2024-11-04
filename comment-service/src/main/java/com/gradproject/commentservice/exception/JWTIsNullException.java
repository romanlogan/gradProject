package com.gradproject.commentservice.exception;

public class JWTIsNullException extends RuntimeException {

    public JWTIsNullException(String message){
        super(message);
    }
}
