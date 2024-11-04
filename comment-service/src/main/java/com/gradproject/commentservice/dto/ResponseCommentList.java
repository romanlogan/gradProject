package com.gradproject.commentservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseCommentList {

    private List<CommentDto> commentDtoList;

    public ResponseCommentList() {
    }

    public ResponseCommentList(List<CommentDto> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }
}
