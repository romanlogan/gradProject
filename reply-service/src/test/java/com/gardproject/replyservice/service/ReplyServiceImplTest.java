package com.gardproject.replyservice.service;

import com.gardproject.replyservice.dto.RequestDelete;
import com.gardproject.replyservice.dto.RequestSave;
import com.gardproject.replyservice.dto.RequestUpdate;
import com.gardproject.replyservice.dto.ResponseReplies;
import com.gardproject.replyservice.entity.Reply;
import com.gardproject.replyservice.repository.ReplyRepository;
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
class ReplyServiceImplTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @DisplayName("save reply")
    void save() {

        RequestSave requestSave = new RequestSave(999, "test Content");
        LocalDateTime createdAt = LocalDateTime.now();
        requestSave.setCreatedAt(createdAt);

        Long savedId = replyService.save(requestSave, "test@test.com");

        Reply reply = replyRepository.findById(savedId).get();
        assertThat(reply).isNotNull();
        assertThat(reply.getCommentId()).isEqualTo(999);
        assertThat(reply.getContent()).isEqualTo("test Content");
        assertThat(reply.getUserEmail()).isEqualTo("test@test.com");
        assertThat(reply.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("get reply dtos by commentId")
    void getReplies() {
        //given
        saveReplies();

        //when
        ResponseReplies response = replyService.getReplies(999L);

        //then
        assertThat(response.getReplyDtoList().size()).isEqualTo(3);
        assertThat(response.getReplyDtoList().get(0).getContent()).isEqualTo("test content4");
        assertThat(response.getReplyDtoList().get(1).getContent()).isEqualTo("test content2");
        assertThat(response.getReplyDtoList().get(2).getContent()).isEqualTo("test content1");
    }

    private Long saveReplies() {
        replyRepository.save(new Reply("test content1", 999L, "test@test.com", LocalDateTime.of(2024, 5, 10, 7, 10, 30)));
        Reply reply = replyRepository.save(new Reply("test content2", 999L, "test@test.com", LocalDateTime.of(2024, 5, 11, 7, 10, 30)));
        replyRepository.save(new Reply("test content3", 998L, "test@test.com", LocalDateTime.of(2024, 5, 12, 7, 10, 30)));
        replyRepository.save(new Reply("test content4", 999L, "test2@test2.com", LocalDateTime.of(2024, 5, 13, 7, 10, 30)));

        return reply.getId();
    }

    @Test
    @DisplayName("delete reply")
    void delete() {
        //given
        Long id = saveReplies();
        RequestDelete request = new RequestDelete(Math.toIntExact(id));

        //when
        replyService.delete(request);

        //then
        List<Reply> replies = replyRepository.findByCommentId(999L);
        assertThat(replies.size()).isEqualTo(2);
        assertThat(replies.get(0).getContent()).isEqualTo("test content1");
        assertThat(replies.get(1).getContent()).isEqualTo("test content4");
    }

    @Test
    @DisplayName("update reply")
    void update() {
        //given
        Long id = saveReplies();
        RequestUpdate request = new RequestUpdate(Math.toIntExact(id),"updated content");

        //when
        Long updatedId = replyService.update(request);

        //then
        Reply reply = replyRepository.findById(updatedId).get();
        assertThat(reply.getContent()).isEqualTo("updated content");

    }
}