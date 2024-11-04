package com.gardproject.replyservice.entity;

import com.gardproject.replyservice.dto.RequestUpdate;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column(length = 500)
    private String content;

    private Long commentId;

    private String userEmail;

//    private Long gameId;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    public Reply() {
    }

    public Reply(String content, Long commentId, String userEmail, LocalDateTime createdAt) {
        this.content = content;
        this.commentId = commentId;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public void update(RequestUpdate requestUpdate) {

        setContent(requestUpdate.getContent());
        setLastModifiedAt(LocalDateTime.now());
    }
}
