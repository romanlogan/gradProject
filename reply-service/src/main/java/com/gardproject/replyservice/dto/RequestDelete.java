package com.gardproject.replyservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDelete {

    Integer replyId;

    public RequestDelete() {
    }

    public RequestDelete(Integer replyId) {
        this.replyId = replyId;
    }
}
