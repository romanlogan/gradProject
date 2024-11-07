package com.gradproject.commentservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RequestUpdate {

    @NotNull(message = "commentId cannot be null")
    private Integer commentId;

    @NotBlank(message = "Please write your comment content")
    @Size(max = 255, min = 1, message = "Please write your comment between 1 and 255 characters.")
    private String content;

    public RequestUpdate() {
    }

    @Builder
    public RequestUpdate(Integer commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }

    public static RequestUpdate create(Integer commentId, String content){

        return RequestUpdate.builder()
                .commentId(commentId)
                .content(content)
                .build();

    }
}
