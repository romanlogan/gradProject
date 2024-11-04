package com.gradproject.commentservice.repository;

import com.gradproject.commentservice.dto.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentDto> getCommentDtoListBy(Long gameId);
}
