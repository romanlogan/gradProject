package com.gradproject.gameservice.exception;

public class JwtNullTokenException extends RuntimeException {


    public JwtNullTokenException(String message){
        super(message);
    }
}
