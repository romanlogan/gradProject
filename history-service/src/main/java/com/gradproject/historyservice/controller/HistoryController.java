package com.gradproject.historyservice.controller;

import com.gradproject.historyservice.constant.ExitType;
import com.gradproject.historyservice.dto.ResponseHistory;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.dto.SaveCardGameRequest;
import com.gradproject.historyservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin
public class HistoryController {

    private final HistoryService historyService;

    // url cardGameSave 로 변경 필요
    @PostMapping("/save")
    public ResponseEntity<Long> saveCardGameHistory(@RequestBody SaveCardGameRequest request) {

        request.setExitType(ExitType.FINISH);

        Long id = historyService.saveCardGame(request);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/getPlayedGameList/{email}")
    public ResponseEntity<ResponseHistory> getPlayedGameList(@PathVariable String email) throws Exception {

        log.info("before retrieve history data");

        ResponseHistory response = historyService.getPlayedGameList(email);

//        try {
//            Thread.sleep(1000);
//            throw new Exception("장애 발생");
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//
//        }

        log.info("add retrieve history data");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/lastSavedHistory")
    public ResponseEntity<LastSavedHistory> getLastSavedHistory(
            @RequestParam Integer gameId,
            @RequestParam String email) {

        LastSavedHistory lastSavedHistory = historyService.getLastSavedHistory(gameId, email);

        return ResponseEntity.status(HttpStatus.OK).body(lastSavedHistory);
    }
}
