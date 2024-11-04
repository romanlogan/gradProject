package com.gradproject.commentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDelete {

    private String commentId;

    public RequestDelete() {
    }

    public RequestDelete(String commentId) {
        this.commentId = commentId;
    }
}
