package com.gardproject.replyservice.repository;

import com.gardproject.replyservice.dto.ReplyDto;

import java.util.List;

public interface ReplyRepositoryCustom {

    List<ReplyDto> getReplyDtosByCommentId(Long commentId);
}
