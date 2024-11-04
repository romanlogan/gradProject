package com.gradproject.commentservice.exception;

public class NoMatchPasswordException extends RuntimeException {

    public NoMatchPasswordException(String message){
        super(message);
    }
}
