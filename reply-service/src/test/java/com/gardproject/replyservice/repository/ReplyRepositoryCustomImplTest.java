package com.gardproject.replyservice.repository;

import com.gardproject.replyservice.dto.ReplyDto;
import com.gardproject.replyservice.entity.Reply;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class ReplyRepositoryCustomImplTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @DisplayName("get reply dtos by commentId")
    void getReplyDtosByCommentId() {

        replyRepository.save(new Reply("test content1", 999L, "test@test.com", LocalDateTime.now()));
        replyRepository.save(new Reply("test content2", 999L, "test@test.com", LocalDateTime.now()));
        replyRepository.save(new Reply("test content3", 999L, "test@test.com", LocalDateTime.now()));
        replyRepository.save(new Reply("test content4", 998L, "test@test.com", LocalDateTime.now()));
        replyRepository.save(new Reply("test content5", 999L, "test2@test2.com", LocalDateTime.now()));

        List<ReplyDto> replyDtos = replyRepository.getReplyDtosByCommentId(999L);

        assertThat(replyDtos).hasSize(4);
    }

}