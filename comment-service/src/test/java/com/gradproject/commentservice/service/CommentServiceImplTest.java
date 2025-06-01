package com.gradproject.commentservice.service;

import com.gradproject.commentservice.client.UserServiceClient;
import com.gradproject.commentservice.dto.*;
import com.gradproject.commentservice.entity.Comment;
import com.gradproject.commentservice.exception.CommentNotExistException;
import com.gradproject.commentservice.exception.UserCommentAlreadyExistException;
import com.gradproject.commentservice.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class CommentServiceImplTest {

    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void createMember() {

//        userServiceClient.createUser(new RequestUser("test1@test1.com","test1","1234"));
//        userServiceClient.createUser(new RequestUser("test2@test2.com","test2","1234"));
//        userServiceClient.createUser(new RequestUser("test3@test3.com", "test3", "1234"));
    }

    @Test
    @DisplayName("save comment")
    void save() {

        RequestSaveComment requestSaveComment = RequestSaveComment.create(999, "test content");
        Long id = commentService.save(requestSaveComment, "test@test.com");

        Comment comment = commentRepository.findById(id).get();
        assertThat(comment.getGameId()).isEqualTo(999);
        assertThat(comment.getContent()).isEqualTo("test content");
    }

    @Test
    @DisplayName("User only can save 1 comment on 1 game")
    void saveDuplicateComment() {

        RequestSaveComment requestSaveComment = RequestSaveComment.create(999, "test content");
        Long id = commentService.save(requestSaveComment, "test@test.com");



        assertThatThrownBy(() -> commentService.save(RequestSaveComment.create(999, "test content2"), "test@test.com"))
                .isInstanceOf(UserCommentAlreadyExistException.class)
                .hasMessage("A comment has already been posted.");
    }

    @Test
    @DisplayName("same user can save comment at different game")
    void saveTwoCommentAtDifferentGame() {

        commentService.save(RequestSaveComment.create(999, "test content"), "test@test.com");
        Long id = commentService.save(RequestSaveComment.create(998, "test content2"), "test@test.com");

        Comment comment = commentRepository.findById(id).get();
        assertThat(comment.getGameId()).isEqualTo(998);
        assertThat(comment.getContent()).isEqualTo("test content2");
    }

    @Test
    @DisplayName("The most recent comment is at the top, when retrieve the comment list")
    void getCommentList() {

        commentRepository.save(Comment.create("test content1", "test@test.com", 999,
                LocalDateTime.of(2024, 7, 20, 5, 30, 30)));
        commentRepository.save(Comment.create("test content2", "test2@test2.com", 999,
                LocalDateTime.of(2024, 7, 21, 7, 30, 30)));
        commentRepository.save(Comment.create("test content3", "test3@test3.com", 999,
                LocalDateTime.of(2024, 7, 22, 7, 30, 31)));

        ResponseCommentList commentList = commentService.getCommentList(999);
        List<CommentDto> commentDtoList = commentList.getCommentDtoList();

        assertThat(commentDtoList.get(0).getContent()).isEqualTo("test content3");
        assertThat(commentDtoList.get(0).getUserEmail()).isEqualTo("test3@test3.com");
        assertThat(commentDtoList.get(1).getContent()).isEqualTo("test content2");
        assertThat(commentDtoList.get(1).getUserEmail()).isEqualTo("test2@test2.com");
        assertThat(commentDtoList.get(2).getContent()).isEqualTo("test content1");
        assertThat(commentDtoList.get(2).getUserEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("update comment")
    void update() {

        Comment comment = commentRepository.save(Comment.create("test content1", "test@test.com", 999,
                LocalDateTime.of(2024, 7, 20, 5, 30, 30)));

        RequestUpdate requestUpdate = RequestUpdate.create(Math.toIntExact(comment.getId()), "modified test content");

        commentService.update(requestUpdate);

        Comment modifiedComment = commentRepository.findById(comment.getId()).get();
        assertThat(modifiedComment.getContent()).isEqualTo("modified test content");
    }

    @Test
    @DisplayName("When updating a non-exist comment, then throw CommentNotExistException")
    void updateNotExistComment() {

        RequestUpdate requestUpdate = RequestUpdate.create(1, "modified test content");

        assertThatThrownBy(() -> commentService.update(requestUpdate))
                .isInstanceOf(CommentNotExistException.class)
                .hasMessage("This comment does not exist.");
    }


}