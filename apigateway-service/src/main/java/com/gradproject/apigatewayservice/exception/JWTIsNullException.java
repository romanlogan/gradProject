package com.gradproject.apigatewayservice.exception;

public class JWTIsNullException extends RuntimeException {

    public JWTIsNullException(String message){
        super(message);
    }
}
