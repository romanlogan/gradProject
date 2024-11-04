package com.gradproject.commentservice.exception;

public class UserCommentAlreadyExistException extends RuntimeException {

    public UserCommentAlreadyExistException(String message){
        super(message);
    }
}
