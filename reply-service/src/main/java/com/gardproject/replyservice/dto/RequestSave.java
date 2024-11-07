package com.gardproject.replyservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class RequestSave {

    @NotNull(message = "commentId cannot be null")
    private Integer commentId;

    @NotBlank(message = "content cannot be blank")
    @Size(max = 255, min = 1, message = "Please write your comment between 1 and 255 characters.")
    private String content;

    private LocalDateTime createdAt;

    public RequestSave() {
    }

    public RequestSave(Integer commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
