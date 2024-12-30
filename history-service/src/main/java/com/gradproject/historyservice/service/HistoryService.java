package com.gradproject.historyservice.service;

import com.gradproject.historyservice.dto.ResponseHistory;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.dto.SaveCardGameRequest;

public interface HistoryService {

    Long saveCardGame(SaveCardGameRequest request);

    Long saveCardGameByKafka(SaveCardGameRequest request,String kafkaMessage);

    ResponseHistory getPlayedGameListByKafka(String email,String kafkaMessage);

    ResponseHistory getPlayedGameList(String email);

    LastSavedHistory getLastSavedHistory(Integer gameId, String email);
}

