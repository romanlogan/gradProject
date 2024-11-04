package com.gradproject.commentservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestUpdate {

    @NotNull(message = "commentId cannot be null")
    private Integer commentId;

    @NotBlank(message = "content cannot be blank")
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
