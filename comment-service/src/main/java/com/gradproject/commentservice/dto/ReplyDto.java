package com.gradproject.commentservice.dto;

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

    public ReplyDto(Long id, String content, String userEmail, LocalDateTime lastCreatedOrModifiedAt) {
        this.id = id;
        this.content = content;
        this.userEmail = userEmail;
        this.lastCreatedOrModifiedAt = lastCreatedOrModifiedAt;
    }
}
