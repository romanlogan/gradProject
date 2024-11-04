package com.gradproject.userservice.exception;

public class NoMatchEmailException extends RuntimeException {

    public NoMatchEmailException(String message){
        super(message);
    }
}
