package com.gradproject.gameservice.client;

import com.gradproject.gameservice.dto.LastSavedHistory;
import com.gradproject.gameservice.dto.SaveHistoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "history-service")
public interface HistoryServiceClient {

    @PostMapping("/save/{id}")
    ResponseEntity<Long> save(@PathVariable("id") Long gameId, SaveHistoryRequest request);

    @GetMapping("/lastSavedHistory")
    ResponseEntity<LastSavedHistory> getLastSavedHistory(@RequestParam Integer gameId, @RequestParam String email);
}
