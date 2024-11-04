package com.gardproject.replyservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestSave {

    private Integer commentId;
    private String content;
    private LocalDateTime createdAt;

    public RequestSave() {
    }

    public RequestSave(Integer commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
