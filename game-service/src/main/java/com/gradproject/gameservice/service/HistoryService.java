package com.gradproject.gameservice.service;

import com.gradproject.gameservice.dto.LastSavedHistory;
import com.gradproject.gameservice.dto.ResponseHistory;
import com.gradproject.gameservice.dto.SaveCardGameRequest;

public interface HistoryService {

    Long saveCardGame(SaveCardGameRequest request);

    Long saveCardGameByKafka(SaveCardGameRequest request,String kafkaMessage);

    ResponseHistory getPlayedGameListByKafka(String email,String kafkaMessage);

    ResponseHistory getPlayedGameList(String email);

    LastSavedHistory getLastSavedHistory(Integer gameId, String email);
}

