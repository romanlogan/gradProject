package com.gradproject.apigatewayservice.exception;

public class JwtNullTokenException extends RuntimeException {


    public JwtNullTokenException(String message){
        super(message);
    }
}
