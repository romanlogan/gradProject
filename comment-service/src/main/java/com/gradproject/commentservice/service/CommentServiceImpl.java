package com.gradproject.commentservice.service;

import com.gradproject.commentservice.client.ReplyServiceClient;
import com.gradproject.commentservice.dto.*;
import com.gradproject.commentservice.entity.Comment;
import com.gradproject.commentservice.exception.CommentNotExistException;
import com.gradproject.commentservice.exception.UserCommentAlreadyExistException;
import com.gradproject.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

        //1명의 회원은 1개의 댓글만 작성 가능하다
        Comment existedComment = commentRepository.findByUserEmailAndGameId(email, Long.valueOf(request.getGameId()));

        if (existedComment != null) {

            throw new UserCommentAlreadyExistException("댓글은 1번만 작성할 수 있습니다.");
        }

        Comment comment = Comment.create(request, email);

        return commentRepository.save(comment).getId();
    }

    @Override
    public ResponseCommentList getCommentList(Long gameId) {

        List<CommentDto> commentDtoList = commentRepository.getCommentDtoListBy(gameId);
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        for (CommentDto commentDto : commentDtoList) {

            //commentID 로 그 댓글의 reply 들을 가져오기

//            ResponseEntity<ResponseReplies> response =
//                    replyServiceClient.getReplies(Math.toIntExact(commentDto.getId()));

            ResponseEntity<ResponseReplies> response =
                    circuitbreaker.run(() -> replyServiceClient.getReplies(Math.toIntExact(commentDto.getId())),
//                            throwable -> ResponseEntity.ok(new ResponseReplies("에러 발생 ")));
                            throwable -> ResponseEntity.ok(ResponseReplies.error()));


//            commentDto 에 붙이기'
            List<ReplyDto> replyDtos = response.getBody().getReplyDtoList();
            commentDto.setReplyDtoList(replyDtos);

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
            throw new CommentNotExistException("존재하지 않는 댓글 입니다.");
        }

        Comment comment = optionalComment.get();
        comment.update(requestUpdate);

        return comment.getId();
    }
}
