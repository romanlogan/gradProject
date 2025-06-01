package com.gardproject.replyservice.service;

import com.gardproject.replyservice.dto.RequestDelete;
import com.gardproject.replyservice.dto.RequestSave;
import com.gardproject.replyservice.dto.RequestUpdate;
import com.gardproject.replyservice.dto.ResponseReplies;

public interface ReplyService {

    Long save(RequestSave requestSave, String email);
    ResponseReplies getReplies(Long commentId);
    void delete(RequestDelete request);
    Long update(RequestUpdate requestUpdate);

}
