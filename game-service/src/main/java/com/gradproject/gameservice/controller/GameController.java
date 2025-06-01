package com.gradproject.gameservice.controller;

import com.gradproject.gameservice.constant.ExitType;
import com.gradproject.gameservice.dto.*;
import com.gradproject.gameservice.exception.LastSaveHistoryNotExistException;
import com.gradproject.gameservice.service.GameService;
import com.gradproject.gameservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/game-service")
@Slf4j
public class GameController {

    private GameService gameService;

    private Environment env;

//    private HistoryServiceClient historyServiceClient;

    private CircuitBreakerFactory circuitBreakerFactory;

    private HistoryService historyService;

    @Autowired
    public GameController(GameService gameService, Environment env, CircuitBreakerFactory circuitBreakerFactory, HistoryService historyService) {
        this.gameService = gameService;
        this.env = env;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.historyService = historyService;
    }

    @GetMapping("/test")
    public  String test() throws Exception {


        return "test";
    }

    @GetMapping("/loading")
    public  String loading(@RequestParam String type , Model model) throws Exception {

        model.addAttribute("type", type);

        return "loading";
    }


    @GetMapping("/main")
    public String main(Model model,
                       HttpServletRequest request) {

        ResponseMain response = gameService.getCommentList(1L);
//        List<CommentDto> commentDtoList = new ArrayList<>();
//        ResponseMain response = new ResponseMain(commentDtoList);

        model.addAttribute("response", response);
        model.addAttribute("loggedInUserId", null);

        return "cardGameMain";
    }

    @GetMapping("/cardGame")
    public String getGame1(@RequestParam(name = "playType") String playType,
            @RequestParam(name = "token") String token
            ,Model model) throws IOException {

        return "cardGame";
    }

    @PostMapping("/save")
    public ResponseEntity<Long> saveCardGameHistory(@RequestBody SaveCardGameRequest request) {

        request.setExitType(ExitType.FINISH);

        Long id = historyService.saveCardGame(request);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/getPlayedGameList/{email}")
    public ResponseEntity<ResponseHistory> getPlayedGameList(@PathVariable String email) throws Exception {

        ResponseHistory response = historyService.getPlayedGameList(email);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//  get last saved history for continue game
    @GetMapping("/lastSavedHistory")
    public ResponseEntity<LastSavedHistory> getLastSavedHistory(
            @RequestParam Integer gameId,
            @RequestParam String email) {

        LastSavedHistory lastSavedHistory = new LastSavedHistory();

        try {
            lastSavedHistory = historyService.getLastSavedHistory(gameId, email);
        } catch (LastSaveHistoryNotExistException e) {

            lastSavedHistory.setErrorMessage(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(lastSavedHistory);
    }

//    save comment by kafka (produce)

}
