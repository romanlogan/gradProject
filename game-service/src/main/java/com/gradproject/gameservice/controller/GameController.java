package com.gradproject.gameservice.controller;

import com.gradproject.gameservice.client.HistoryServiceClient;
import com.gradproject.gameservice.dto.ResponseMain;
import com.gradproject.gameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/game-service")
@Slf4j
public class GameController {

    private GameService gameService;

    private Environment env;

    private HistoryServiceClient historyServiceClient;

    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public GameController(GameService gameService, Environment env, HistoryServiceClient historyServiceClient, CircuitBreakerFactory circuitBreakerFactory) {

        this.gameService = gameService;
        this.env = env;
        this.historyServiceClient = historyServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/loading")
    public  String loading(@RequestParam String type , Model model) throws Exception {

        model.addAttribute("type", type);

        return "loading";
    }


    @GetMapping("/main")
    public String main(
                       Model model,
                       HttpServletRequest request) {

        ResponseMain response = gameService.getCommentList(1L);

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


}
