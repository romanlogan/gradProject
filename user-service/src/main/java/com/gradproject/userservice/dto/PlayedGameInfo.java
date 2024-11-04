package com.gradproject.userservice.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlayedGameInfo {


    private Long gameId;

    private String gameName;

    private Long totalPlayedTimeByMin;

    private LocalDateTime lastPlayedDate;

    public PlayedGameInfo() {
    }

    @Builder
    public PlayedGameInfo(Long gameId, String gameName, Long totalPlayedTimeByMin, LocalDateTime lastPlayedDate) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.totalPlayedTimeByMin = totalPlayedTimeByMin;
        this.lastPlayedDate = lastPlayedDate;
    }

    public static PlayedGameInfo of(Long gameId, Long totalPlayedTimeByMin, LocalDateTime lastPlayedDate) {

        return PlayedGameInfo.builder()
                .gameId(gameId)
                .gameName("CARD_GAME")
                .totalPlayedTimeByMin(totalPlayedTimeByMin)
                .lastPlayedDate(lastPlayedDate)
                .build();
    }
}
