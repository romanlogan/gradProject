package com.gradproject.commentservice.repository;

import com.gradproject.commentservice.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    EntityManager em;

//    @BeforeEach
//    void createComment() {
//        commentRepository.save(new Comment("content1", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 0, 0)));
//        commentRepository.save(new Comment("content2", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 0, 30)));
//        commentRepository.save(new Comment("content3", "asdf@asdf.com", 1L, LocalDateTime.of(2024, 7, 20, 5, 30, 30)));
//        commentRepository.save(new Comment("content4", "test@test.com", 1L, LocalDateTime.of(2024, 8, 20, 5, 30, 30)));
//        commentRepository.save(new Comment("content5", "qwer@qwer.com", 1L, LocalDateTime.of(2024, 9, 20, 5, 30, 30)));
//    }


    @Test
    @DisplayName("delete comment when gives commentId")
    void delete() {

        Comment comment = commentRepository.save(new Comment("content1", "asdf@asdf.com", 1, LocalDateTime.of(2024, 7, 20, 5, 0, 0)));
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);

        commentRepository.deleteById(comment.getId());

        List<Comment> deletedComments = commentRepository.findAll();
        assertThat(deletedComments).hasSize(0);

    }
}