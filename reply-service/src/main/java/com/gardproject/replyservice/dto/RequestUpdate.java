package com.gardproject.replyservice.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestUpdate {

    Integer replyId;
    String content;

    public RequestUpdate() {
    }

    public RequestUpdate(Integer replyId, String content) {
        this.replyId = replyId;
        this.content = content;
    }
}
