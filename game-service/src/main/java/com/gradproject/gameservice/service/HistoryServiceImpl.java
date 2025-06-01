package com.gradproject.gameservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.gameservice.dto.LastSavedHistory;
import com.gradproject.gameservice.dto.PlayedGameInfo;
import com.gradproject.gameservice.dto.ResponseHistory;
import com.gradproject.gameservice.dto.SaveCardGameRequest;
import com.gradproject.gameservice.entity.CardGameHistory;
import com.gradproject.gameservice.entity.History;
import com.gradproject.gameservice.exception.LastSaveHistoryNotExistException;
import com.gradproject.gameservice.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService{

    private final HistoryRepository historyRepository;

    @Override
    public Long saveCardGame(SaveCardGameRequest request) {

        CardGameHistory history = CardGameHistory.create(request);
        return historyRepository.save(history).getId();
    }

    @Override
//    @KafkaListener(topics = "saveGameHistory-topic")
    public Long saveCardGameByKafka(SaveCardGameRequest request,String kafkaMessage) {

        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }


        CardGameHistory history = CardGameHistory.create(request);
        CardGameHistory savedHistory = historyRepository.save(history);

        return savedHistory.getId();
    }


//    by feign
    @Override
    public ResponseHistory getPlayedGameList(String email){

        List<History> histories = getHistories(email);

        Map<Integer, PlayedGameInfo> playedGameInfoMap = getPlayedGameInfoMap(histories);

        return ResponseHistory.create(playedGameInfoMap);
    }


    @Override
//    @KafkaListener(topics = "getPlayedGameList-topic")
    public ResponseHistory getPlayedGameListByKafka(String email,String kafkaMessage){

        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }


        Map<Integer, PlayedGameInfo> playedGameInfoMap = getPlayedGameInfoMap(getHistories(email));
        return ResponseHistory.create(playedGameInfoMap);
    }


    private static Map<Integer, PlayedGameInfo> getPlayedGameInfoMap(List<History> histories) {

        Map<Integer, PlayedGameInfo> playedGameInfoMap = new HashMap<>();

        for (History history : histories) {

            Integer gameId = history.getGameId();
            LocalDateTime endTime = history.getEndTime();

//          if exist that last played game history
            if (playedGameInfoMap.containsKey(gameId)) {

                PlayedGameInfo playedGameInfo = playedGameInfoMap.get(gameId);
                //get total time for played game
                Long totalTime = getRenewalTotalTime(history, playedGameInfo);
                playedGameInfo.setTotalPlayedTimeByMin(totalTime);

                //change last played date
                renewLastFinishPlayingDateTime(endTime, playedGameInfo);

                playedGameInfoMap.replace(gameId, playedGameInfo);

            } else {

                long minutesDiff = getPlayedTimeByMin(history);
                PlayedGameInfo playedGameInfo = PlayedGameInfo.create(gameId, minutesDiff, endTime);
                playedGameInfoMap.put(gameId, playedGameInfo);
            }
        }
        return playedGameInfoMap;
    }

    private static void renewLastFinishPlayingDateTime(LocalDateTime endTime, PlayedGameInfo playedGameInfo) {
        LocalDateTime lastFinishPlayingDateTime = playedGameInfo.getLastFinishPlayingDateTime();
        if (endTime.isAfter(lastFinishPlayingDateTime)) {
            playedGameInfo.setLastFinishPlayingDateTime(endTime);
        }
    }

    private static Long getRenewalTotalTime(History history, PlayedGameInfo playedGameInfo) {
        Long totalTime = playedGameInfo.getTotalPlayedTimeByMin();
        long minutesDiff = getPlayedTimeByMin(history);
        totalTime += minutesDiff;
        return totalTime;
    }

    private static long getPlayedTimeByMin(History history) {
        Duration duration = Duration.between(history.getStartTime(), history.getEndTime());
        long minutesDiff = duration.toMinutes();
        return minutesDiff;
    }

    private List<History> getHistories(String email) {

        List<History> histories = historyRepository.findByUserEmail(email);
        return histories;
    }

    @Override
    public LastSavedHistory getLastSavedHistory(Integer gameId, String email) {

        CardGameHistory history = historyRepository.getLastSavedHistory(gameId, email);

        if(history == null){
            throw new LastSaveHistoryNotExistException("There is no recent play history.");
        }

        return LastSavedHistory.create(history);
    }
}
