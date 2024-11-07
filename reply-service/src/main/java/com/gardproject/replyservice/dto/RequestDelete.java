package com.gardproject.replyservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestDelete {

    @NotNull(message = "replyId cannot be null")
    Integer replyId;

    public RequestDelete() {
    }

    public RequestDelete(Integer replyId) {
        this.replyId = replyId;
    }
}
