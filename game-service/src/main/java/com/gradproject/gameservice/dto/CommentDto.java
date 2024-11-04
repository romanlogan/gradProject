package com.gradproject.gameservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDto {

    private Long id;
    private String content;
    private String userEmail;
    private Long gameId;
    private LocalDateTime createdAt;

    private List<ReplyDto> replyDtoList;



}
