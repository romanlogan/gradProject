package com.gradproject.gameservice.client;

import com.gradproject.gameservice.dto.ResponseCommentList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-service")
public interface CommentServiceClient {

    @GetMapping("/getCommentList/{gameId}")
    ResponseEntity<ResponseCommentList> getCommentList(@PathVariable Integer gameId);


}
