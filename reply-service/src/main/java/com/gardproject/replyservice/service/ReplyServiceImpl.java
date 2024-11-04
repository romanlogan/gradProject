package com.gardproject.replyservice.service;

import com.gardproject.replyservice.dto.*;
import com.gardproject.replyservice.entity.Reply;
import com.gardproject.replyservice.exception.ReplyNotExistException;
import com.gardproject.replyservice.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Long save(RequestSave requestSave, String email) {

        Reply reply = new Reply(requestSave.getContent(), Long.valueOf(requestSave.getCommentId()), email, requestSave.getCreatedAt());

        Reply savedReply = replyRepository.save(reply);
        return savedReply.getId();
    }

    public ResponseReplies getReplies(Long commentId){

        List<ReplyDto> replyDtos = replyRepository.getReplyDtosByCommentId(commentId);
        ResponseReplies response = new ResponseReplies(replyDtos);

        return response;
    }


    @Override
    public void delete(RequestDelete request) {

        replyRepository.deleteById(Long.valueOf(request.getReplyId()));
    }

    @Override
    public Long update(RequestUpdate requestUpdate) {

        Optional<Reply> optionalReply = replyRepository.findById(Long.valueOf(requestUpdate.getReplyId()));

        if (optionalReply.isEmpty()) {
//            throw new CommentNotExistException("존재하지 않는 댓글 입니다.");
            throw new ReplyNotExistException("존재하지 않는 reply 입니다.");
        }

        Reply reply = optionalReply.get();
        reply.update(requestUpdate);

        return reply.getId();
    }

}
