package com.gradproject.commentservice.service;

import com.gradproject.commentservice.dto.RequestSaveComment;
import com.gradproject.commentservice.dto.RequestUpdate;
import com.gradproject.commentservice.dto.ResponseCommentList;

public interface CommentService {

    Long save(RequestSaveComment request, String email);

    ResponseCommentList getCommentList(Integer gameId);

    void delete(Long commentId);

    Long update(RequestUpdate requestUpdate);

}
