package com.gradproject.commentservice.exception;

public class JwtNullTokenException extends RuntimeException {


    public JwtNullTokenException(String message){
        super(message);
    }
}
