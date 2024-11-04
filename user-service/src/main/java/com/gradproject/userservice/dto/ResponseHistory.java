package com.gradproject.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseHistory {

    Map<Long, PlayedGameInfo> playedGameInfoMap = new HashMap<>();

    public ResponseHistory() {
    }

    @Builder
    public ResponseHistory(Map<Long, PlayedGameInfo> playedGameInfoMap) {
        this.playedGameInfoMap = playedGameInfoMap;
    }

    public static ResponseHistory create(Map<Long, PlayedGameInfo> playedGameInfoMap) {

        return ResponseHistory.builder()
                .playedGameInfoMap(playedGameInfoMap)
                .build();
    }

    public static ResponseHistory createEmpty() {

//        this.playedGameInfoMap = new HashMap<>();

        return ResponseHistory.builder()
                .playedGameInfoMap(new HashMap<>())
                .build();
    }

}
