package com.gradproject.userservice.exception;

public class NoMatchPasswordException extends RuntimeException {
    public NoMatchPasswordException(String message){
        super(message);
    }
}
