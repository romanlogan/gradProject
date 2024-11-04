package com.gradproject.userservice.client;

import com.gradproject.userservice.dto.ResponseHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "history-service")
public interface HistoryServiceClient {

    @GetMapping("/getPlayedGameList/{email}")
    ResponseEntity<ResponseHistory> getPlayedGameList(@PathVariable String email);
}
