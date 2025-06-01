package com.gradproject.commentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.commentservice.client.ReplyServiceClient;
import com.gradproject.commentservice.dto.*;
import com.gradproject.commentservice.entity.Comment;
import com.gradproject.commentservice.exception.CommentNotExistException;
import com.gradproject.commentservice.exception.UserCommentAlreadyExistException;
import com.gradproject.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final ReplyServiceClient replyServiceClient;

    public CommentServiceImpl(CommentRepository commentRepository,
                              CircuitBreakerFactory circuitBreakerFactory,
                              ReplyServiceClient replyServiceClient) {

        this.commentRepository = commentRepository;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.replyServiceClient = replyServiceClient;
    }

    public Long save(RequestSaveComment request, String email) {

        // A member can post only one comment.
        Comment existedComment = commentRepository.findByUserEmailAndGameId(email, (request.getGameId()));

        if (existedComment != null) {

            throw new UserCommentAlreadyExistException("A comment has already been posted.");
        }

        Comment comment = Comment.create(request, email);

        return commentRepository.save(comment).getId();
    }

    @KafkaListener(topics = "saveComment-topic")
    public void saveByKafka(String kafkaMessage) throws InterruptedException {

        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();

//        string to json
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        Integer gameId = (Integer) map.get("gameId");
        String content = (String) map.get("content");
        String userEmail = (String) map.get("userEmail");


//         A member can post only one comment.
        Comment existedComment = commentRepository.findByUserEmailAndGameId(userEmail, gameId);

        if (existedComment != null) {

            throw new UserCommentAlreadyExistException("A comment has already been posted.");
        }

        Comment comment = Comment.create(content,userEmail,gameId, LocalDateTime.now());
        commentRepository.save(comment);
    }


    @Override
    public ResponseCommentList getCommentList(Integer gameId) {

        List<CommentDto> commentDtoList = commentRepository.getCommentDtoListBy(gameId);
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        for (CommentDto commentDto : commentDtoList) {

            //Get the replies to that comment using commentID
            ResponseEntity<ResponseReplies> response =
                    circuitbreaker.run(() -> replyServiceClient.getReplies(Math.toIntExact(commentDto.getId())),
                            throwable -> ResponseEntity.ok(ResponseReplies.error()));

            commentDto.setReplyDtoList(response.getBody().getReplyDtoList());
        }
        return new ResponseCommentList(commentDtoList);
    }


    @Override
    public void delete(Long commentId) {

        commentRepository.deleteById(commentId);
    }

    @Override
    public Long update(RequestUpdate requestUpdate) {

        Optional<Comment> optionalComment = commentRepository.findById(Long.valueOf(requestUpdate.getCommentId()));

        if (optionalComment.isEmpty()) {
            throw new CommentNotExistException("This comment does not exist.");
        }
        Comment comment = optionalComment.get();
        comment.update(requestUpdate);

        return comment.getId();
    }
}
