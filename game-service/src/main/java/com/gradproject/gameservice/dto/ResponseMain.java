package com.gradproject.gameservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseMain {

     private List<CommentDto> commentDtoList;

    public ResponseMain() {
    }

    public ResponseMain(List<CommentDto> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

}
