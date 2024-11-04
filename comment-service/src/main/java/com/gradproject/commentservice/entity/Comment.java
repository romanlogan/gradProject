package com.gradproject.commentservice.entity;

import com.gradproject.commentservice.dto.RequestSaveComment;
import com.gradproject.commentservice.dto.RequestUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 500)
    private String content;

    private String userEmail;

    private Long gameId;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    public Comment() {
    }

    @Builder
    public Comment(String content, String userEmail, Long gameId, LocalDateTime createdAt) {
        this.content = content;
        this.userEmail = userEmail;
        this.gameId = gameId;
        this.createdAt = createdAt;
    }

    public static Comment create(RequestSaveComment request, String userEmail) {
        return Comment.builder()
                .content(request.getContent())
                .userEmail(userEmail)
                .gameId(Long.valueOf(request.getGameId()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Comment create(String content, String userEmail, Long gameId, LocalDateTime createdAt) {

        return Comment.builder()
                .content(content)
                .userEmail(userEmail)
                .gameId(gameId)
                .createdAt(createdAt)
                .build();
    }

    public void update(RequestUpdate requestUpdate) {

        this.content = requestUpdate.getContent();
        this.lastModifiedAt = LocalDateTime.now();
    }
}
