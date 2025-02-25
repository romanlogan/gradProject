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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/test")
    @ResponseBody
    public String loadExternalHtml() throws Exception {
        // 외부 파일 경로 설정 (예: /Users/username/external_folder/loading.html)
        // 파일 내용을 읽어서 반환
        String filePath = env.getProperty("asterion.path");
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }


    @GetMapping("/loading")
    public  String loading(@RequestParam String type , Model model) throws Exception {

//        loading 화면 경로
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("file:///Users/lee/Desktop/gradFront/WebGame_CSIE/loading/loading.html?type=new");

//        Path path = Paths.get("/Users/lee/Desktop/gradFront/WebGame_CSIE/loading/loading.html");
//
//        String content = Files.readString(path, StandardCharsets.UTF_8);
//
//        content = content.replace("{{param}}", param);

//        return ResponseEntity.ok()
//                .contentType(MediaType.TEXT_HTML)
//                .body(content);

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
//    @ResponseBody
    public String getGame1(@RequestParam(name = "playType") String playType,
            @RequestParam(name = "token") String token
            ,Model model) throws IOException {




//        String userEmail = getSubjectInJwt(token);
        //querystring 에 play type 을 보내서
        // playType 이 new 이면 new 로직으로
        // playType 이 continue 이면 continue 로직으로
//        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
//
//
//        if (playType.equals("new")) {
//
////          basic deck 을 가져온다
//            List<Integer> basicDeck = List.of(31, 32, 33, 46, 49, 52, 55, 60);
//
//            LastSavedHistory lastSavedHistory = new LastSavedHistory();
//            lastSavedHistory.setUserEmail(userEmail);
//
//            model.addAttribute("basicDeck",basicDeck);
//            model.addAttribute("lastSavedHistory", lastSavedHistory);
//
//        } else if (playType.equals("continue")) {
//
//            ResponseEntity<LastSavedHistory> response =
//                    circuitbreaker.run(() -> historyServiceClient.getLastSavedHistory(1, userEmail),
//                            throwable -> ResponseEntity.ok(LastSavedHistory.error()));
//
//            LastSavedHistory lastSavedHistory = response.getBody();
//
//
//
//            model.addAttribute("basicDeck","null");
//            model.addAttribute("lastSavedHistory", lastSavedHistory);
//        }
//
//        model.addAttribute("userEmail", userEmail);
//        model.addAttribute("startTime", LocalDateTime.now());
////        return "cardGame";

//        RESPONSEBODY 를 확인할것

//        String filePath = env.getProperty("asterion.path");
//        return new String(Files.readAllBytes(Paths.get(filePath)));

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
