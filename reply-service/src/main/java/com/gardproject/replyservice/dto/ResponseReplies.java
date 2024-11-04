package com.gardproject.replyservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseReplies {

    private List<ReplyDto> replyDtoList;

    private String errorMessage;

    public ResponseReplies() {
    }

    public ResponseReplies(List<ReplyDto> replyDtoList) {

        this.replyDtoList = replyDtoList;
    }

    public ResponseReplies(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Builder
    public ResponseReplies(List<ReplyDto> replyDtoList, String errorMessage) {
        this.replyDtoList = replyDtoList;
        this.errorMessage = errorMessage;
    }

    public static ResponseReplies error() {

        return ResponseReplies.builder()
                .errorMessage("에러 발생 ")
                .build();
    }
}
