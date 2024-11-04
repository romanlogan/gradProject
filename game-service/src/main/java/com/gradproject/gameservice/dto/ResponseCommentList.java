package com.gradproject.gameservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseCommentList {

    private List<CommentDto> commentDtoList;

    private String errorMessage;

    public ResponseCommentList() {
    }

    @Builder
    public ResponseCommentList(List<CommentDto> commentDtoList,String errorMessage) {
        this.commentDtoList = commentDtoList;
        this.errorMessage = errorMessage;
    }

    public static ResponseCommentList createEmpty() {

        return ResponseCommentList.builder()
                .commentDtoList(new ArrayList<>())
                .build();
    }

    public static ResponseCommentList error() {

        return ResponseCommentList.builder()
                .errorMessage("에러 발생 ")
                .build();
    }


}
