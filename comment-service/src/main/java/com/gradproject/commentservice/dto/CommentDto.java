package com.gradproject.commentservice.dto;

import com.querydsl.core.annotations.QueryProjection;
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
    private Integer gameId;
    private LocalDateTime createdAt;

    private List<ReplyDto> replyDtoList;

    public CommentDto() {
    }

    @QueryProjection
    public CommentDto(Long id, String content, String userEmail, Integer gameId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userEmail = userEmail;
        this.gameId = gameId;
        this.createdAt = createdAt;
    }
}
