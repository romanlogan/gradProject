package com.gradproject.commentservice.exception;

public class CommentNotExistException extends RuntimeException {

    public CommentNotExistException(String message){
        super(message);
    }
}
