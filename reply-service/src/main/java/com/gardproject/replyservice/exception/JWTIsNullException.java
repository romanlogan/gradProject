package com.gardproject.replyservice.exception;

public class JWTIsNullException extends RuntimeException {

    public JWTIsNullException(String message){
        super(message);
    }
}
