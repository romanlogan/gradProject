package com.gradproject.commentservice.client;

import com.gradproject.commentservice.dto.ResponseReplies;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reply-service")
public interface ReplyServiceClient {

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<ResponseReplies> getReplies(@PathVariable Integer commentId);
}
