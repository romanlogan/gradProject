package com.gradproject.gameservice.controller;

import com.gradproject.gameservice.client.HistoryServiceClient;
import com.gradproject.gameservice.constant.ExitType;
import com.gradproject.gameservice.dto.*;
import com.gradproject.gameservice.exception.JwtNullTokenException;
import com.gradproject.gameservice.service.GameService;
import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/main")
    public String main(
                       Model model,
                       HttpServletRequest request) {

        log.info("called main");

        Long gameId = 1L;
        ResponseMain response = gameService.getCommentList(gameId);
        String loggedInUserId = null;


        model.addAttribute("response", response);
        model.addAttribute("loggedInUserId", loggedInUserId);

        return "cardGameMain";
    }

//    @GetMapping("/main/{token}")
//    public String main(@PathVariable("token") String token,
//                       Model model,
//                       HttpServletRequest request) {
//
//        log.info("called main with token"+ token);
////        jwt 를 다
//        String loggedInUserId;
//
//        try {
//            loggedInUserId = getSubjectInJwt(token);
//        } catch (ExpiredJwtException e) {
//
////            expired jwt 이면 바로 로그인 ㅇ로 보내는것이 아니라 로그인이 되지 않은 유저라고 인식하면 됨
////            return "loginForm";
//            loggedInUserId = null;
////            model.addAttribute("error")
//        }
//
//        Long gameId = 1L;
//        ResponseMain response = gameService.getCommentList(gameId);
//
//        model.addAttribute("response", response);
//        model.addAttribute("loggedInUserId", loggedInUserId);
//
//        return "cardGameMain";
//    }


    @GetMapping("/cardGame")
    public String getGame1(@RequestParam(name = "playType") String playType,
            @RequestParam(name = "token") String token,
            Model model){

        String userEmail = getSubjectInJwt(token);
        //querystring 에 play type 을 보내서
        // playType 이 new 이면 new 로직으로
        // playType 이 continue 이면 continue 로직으로
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        if (playType.equals("new")) {

//          basic deck 을 가져온다
            List<Integer> basicDeck = List.of(31, 32, 33, 46, 49, 52, 55, 60);
            model.addAttribute("basicDeck",basicDeck);
            model.addAttribute("lastSavedHistory", "null");


        } else if (playType.equals("continue")) {

//            history-service 에서 그 user email 의 마지막 플레이 기록과 마지막 플레이 정보들()을 가져온다
//
//            resilience4j 필요

            ResponseEntity<LastSavedHistory> response =
                    circuitbreaker.run(() -> historyServiceClient.getLastSavedHistory(1, userEmail),
                            throwable -> ResponseEntity.ok(LastSavedHistory.error()));

//            ResponseEntity<LastSavedHistory> response = historyServiceClient.getLastSavedHistory(1, userEmail);

            LastSavedHistory lastSavedHistory = response.getBody();
            model.addAttribute("basicDeck","null");
            model.addAttribute("lastSavedHistory", lastSavedHistory);
        }

        model.addAttribute("userEmail", userEmail);
        model.addAttribute("startTime", LocalDateTime.now());
        return "cardGame";
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody SaveHistoryRequest request){

        request.setExitType(ExitType.FINISH);
        Long id = gameService.save(request);

        return new ResponseEntity(id, HttpStatus.OK);
    }

    //가장 최신의 게임을 가져오는 코드
    @GetMapping("/getRecentlyPlayedGame/{email}")
    public ResponseEntity<ResponseGame> getRecentlyPlayedGame(@PathVariable("email") String email) {

        ResponseGame responseGame = gameService.getRecentlyPlayedGameBy(email);

        return ResponseEntity.status(HttpStatus.OK).body(responseGame);
    }


    private String getSubjectInJwt(String token) {

        if (token == null) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null)");
        }

        String replacedToken = token.replace("Bearer", "");

        if (replacedToken.equals("null")) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null string token)");
        }

        String subject = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(replacedToken).getBody()
                .getSubject();

        return subject;
    }
}
