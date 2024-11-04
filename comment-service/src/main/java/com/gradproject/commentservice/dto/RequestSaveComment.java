package com.gradproject.commentservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RequestSaveComment {

    @NotNull(message = "gameId cannot be null")
    private Integer gameId;

    @NotBlank(message = "content cannot be blank")
    @Size(max = 255, min = 1, message = "Please write your comment between 1 and 255 characters.")
    private String content;

    public RequestSaveComment() {
    }

    @Builder
    public RequestSaveComment(Integer gameId, String content) {
        this.gameId = gameId;
        this.content = content;
    }

    public static RequestSaveComment create(Integer gameId, String content){

        return RequestSaveComment.builder()
                .gameId(gameId)
                .content(content)
                .build();

    }


}
