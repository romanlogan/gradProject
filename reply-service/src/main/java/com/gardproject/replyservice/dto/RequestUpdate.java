package com.gardproject.replyservice.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RequestUpdate {

    @NotNull(message = "replyId cannot be null")
    Integer replyId;

    @NotBlank(message = "content cannot be blank")
    @Size(max = 255, min = 1, message = "Please write your comment between 1 and 255 characters.")
    String content;

    public RequestUpdate() {
    }

    public RequestUpdate(Integer replyId, String content) {
        this.replyId = replyId;
        this.content = content;
    }
}
