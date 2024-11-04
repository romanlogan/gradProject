package com.gradproject.historyservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlayedGameInfo {

    private Integer gameId;

    private String gameName;

    private Long totalPlayedTimeByMin;

    private LocalDateTime lastFinishPlayingDateTime;

    public PlayedGameInfo() {
    }

    @Builder
    public PlayedGameInfo(Integer gameId, String gameName, Long totalPlayedTimeByMin, LocalDateTime lastFinishPlayingDateTime) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.totalPlayedTimeByMin = totalPlayedTimeByMin;
        this.lastFinishPlayingDateTime = lastFinishPlayingDateTime;
    }

    public static PlayedGameInfo create(Integer gameId, Long totalPlayedTimeByMin, LocalDateTime lastFinishPlayingDateTime) {

        return PlayedGameInfo.builder()
                .gameId(gameId)
                .gameName("CARD_GAME")
                .totalPlayedTimeByMin(totalPlayedTimeByMin)
                .lastFinishPlayingDateTime(lastFinishPlayingDateTime)
                .build();
    }

}
