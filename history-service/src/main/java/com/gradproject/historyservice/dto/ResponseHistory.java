package com.gradproject.historyservice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseHistory {

    Map<Integer, PlayedGameInfo> playedGameInfoMap = new HashMap<>();

    public ResponseHistory() {
    }

    @Builder
    public ResponseHistory(Map<Integer, PlayedGameInfo> playedGameInfoMap) {
        this.playedGameInfoMap = playedGameInfoMap;
    }

    public static ResponseHistory create(Map<Integer, PlayedGameInfo> playedGameInfoMap) {

        return ResponseHistory.builder()
                .playedGameInfoMap(playedGameInfoMap)
                .build();
    }

}
