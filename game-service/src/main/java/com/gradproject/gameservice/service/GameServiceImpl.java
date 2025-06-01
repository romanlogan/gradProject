package com.gradproject.gameservice.service;


import com.gradproject.gameservice.client.CommentServiceClient;
import com.gradproject.gameservice.client.HistoryServiceClient;
import com.gradproject.gameservice.dto.*;
import com.gradproject.gameservice.entity.CardGame;
import com.gradproject.gameservice.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@Slf4j
public class GameServiceImpl implements GameService {

    GameRepository gameRepository;
    Environment env;
    RestTemplate restTemplate;
    CommentServiceClient commentServiceClient;
    HistoryServiceClient historyServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public GameServiceImpl(GameRepository gameRepository,
                           Environment environment,
                           CommentServiceClient commentServiceClient,
                           HistoryServiceClient historyServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory,
                           RestTemplate restTemplate){

        this.gameRepository = gameRepository;
        this.env = environment;
        this.restTemplate = restTemplate;
        this.commentServiceClient = commentServiceClient;
        this.historyServiceClient = historyServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public Long save(SaveHistoryRequest request) {

        CardGame game = CardGame.create(request);
        Long id = gameRepository.save(game).getId();

        // feign client
        historyServiceClient.save(id, request);

        return id;
    }

    @Override
    public ResponseGame getRecentlyPlayedGameBy(String email) {

//        Game game = gameRepository.findRecentlyPlayedGame(email);

        return ResponseGame.create(null);
    }

    @Override
    public ResponseMain getCommentList(Long gameId) {

        log.info("before call comment service");

        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        ResponseEntity<ResponseCommentList> response =
                circuitbreaker.run(() -> commentServiceClient.getCommentList(Math.toIntExact(gameId)),
//                throwable -> ResponseEntity.ok(ResponseCommentList.createEmpty()));
                throwable -> ResponseEntity.ok(ResponseCommentList.error()));

        log.info("after call comment service");

        return new ResponseMain(response.getBody().getCommentDtoList());
    }
}
