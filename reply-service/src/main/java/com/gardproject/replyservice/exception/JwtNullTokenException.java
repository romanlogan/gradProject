package com.gardproject.replyservice.exception;

public class JwtNullTokenException extends RuntimeException {


    public JwtNullTokenException(String message){
        super(message);
    }
}
