package com.gradproject.commentservice.repository;

import com.gradproject.commentservice.dto.CommentDto;
import com.gradproject.commentservice.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.yml")
class CommentRepositoryCustomImplTest {


    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void createComment() {
        commentRepository.save(new Comment("content1", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 0, 0)));
        commentRepository.save(new Comment("content2", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 0, 30)));
        commentRepository.save(new Comment("content3", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 30, 30)));
        commentRepository.save(new Comment("content4", "test@test.com", 1L, LocalDateTime.of(2024, 8, 20, 5, 30, 30)));
        commentRepository.save(new Comment("content5", "qwer@qwer.com", 1L, LocalDateTime.of(2024, 9, 20, 5, 30, 30)));
    }

    @Test
    void getCommentDtoListBy() {

        List<CommentDto> commentDtoList = commentRepository.getCommentDtoListBy(1L);

        assertThat(commentDtoList.get(0).getContent()).isEqualTo("content5");
        assertThat(commentDtoList.get(1).getContent()).isEqualTo("content4");
        assertThat(commentDtoList.get(2).getContent()).isEqualTo("content3");
        assertThat(commentDtoList.get(3).getContent()).isEqualTo("content2");
        assertThat(commentDtoList.get(4).getContent()).isEqualTo("content1");

    }
}