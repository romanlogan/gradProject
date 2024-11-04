package com.gradproject.commentservice.exception;

public class NoMatchEmailException extends RuntimeException {

    public NoMatchEmailException(String message){
        super(message);
    }
}
