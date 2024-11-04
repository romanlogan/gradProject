package com.gardproject.replyservice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyDto {

    private Long id;
    private String content;
    private String userEmail;
    private LocalDateTime lastCreatedOrModifiedAt;

    public ReplyDto() {
    }

    @QueryProjection
    public ReplyDto(Long id, String content, String userEmail, LocalDateTime lastCreatedOrModifiedAt) {
        this.id = id;
        this.content = content;
        this.userEmail = userEmail;
        this.lastCreatedOrModifiedAt = lastCreatedOrModifiedAt;
    }
}
