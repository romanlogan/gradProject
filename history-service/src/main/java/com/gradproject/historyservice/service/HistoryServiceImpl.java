package com.gradproject.historyservice.service;

import com.gradproject.historyservice.dto.PlayedGameInfo;
import com.gradproject.historyservice.dto.ResponseHistory;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.dto.SaveCardGameRequest;
import com.gradproject.historyservice.entity.CardGameHistory;
import com.gradproject.historyservice.entity.History;
import com.gradproject.historyservice.exception.LastSaveHistoryNotExistException;
import com.gradproject.historyservice.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

//            이미 저장되어 있는 정보가 있으면
            if (playedGameInfoMap.containsKey(gameId)) {

                PlayedGameInfo playedGameInfo = playedGameInfoMap.get(gameId);
                // 총 플레이 시간 구하는 로직
                Long totalTime = getRenewalTotalTime(history, playedGameInfo);
                playedGameInfo.setTotalPlayedTimeByMin(totalTime);

                //마지막으로 플레이한 날짜 변경 로직
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
            throw new LastSaveHistoryNotExistException("최근 플레이한 기록이 없습니다.");
        }

        return LastSavedHistory.create(history);
    }
}
