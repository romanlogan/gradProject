package com.gradproject.historyservice.exception;

public class LastSaveHistoryNotExistException extends RuntimeException {

    public LastSaveHistoryNotExistException(String message) {
        super(message);
    }
}
