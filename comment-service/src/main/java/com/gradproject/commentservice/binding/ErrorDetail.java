package com.gradproject.commentservice.binding;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail<T> {

//    private String errorName;
    private T rejectedValue;
    private String message;

    public ErrorDetail() {
    }

    public ErrorDetail(T rejectedData, String message) {
        this.rejectedValue = rejectedData;
        this.message = message;
    }
}
