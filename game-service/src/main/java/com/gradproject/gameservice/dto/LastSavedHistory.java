package com.gradproject.gameservice.dto;

import com.gradproject.gameservice.constant.ExitType;
import com.gradproject.gameservice.entity.CardGameHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LastSavedHistory {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExitType exitType;
    private Integer gameId;
    private String userEmail;

    private Integer hp;
    private String cards;
    private String route;
    private Map<String, Integer> enemies = new HashMap<>();

    private String errorMessage;

    public LastSavedHistory() {
    }

    @Builder
    public LastSavedHistory(Long id, LocalDateTime startTime, LocalDateTime endTime, ExitType exitType, Integer gameId, String userEmail, Integer hp, String cards, String route, Map<String, Integer> enemies, String errorMessage) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exitType = exitType;
        this.gameId = gameId;
        this.userEmail = userEmail;
        this.hp = hp;
        this.cards = cards;
        this.route = route;
        this.enemies = enemies;
        this.errorMessage = errorMessage;
    }

    public static LastSavedHistory create(CardGameHistory cardGameHistory) {

        return LastSavedHistory.builder()
                .id(cardGameHistory.getId())
                .startTime(cardGameHistory.getStartTime())
                .endTime(cardGameHistory.getEndTime())
                .exitType(cardGameHistory.getExitType())
                .gameId(cardGameHistory.getGameId())
                .userEmail(cardGameHistory.getUserEmail())
                .hp(cardGameHistory.getHp())
                .cards(cardGameHistory.getCards())
                .route(cardGameHistory.getRoute())
                .enemies(cardGameHistory.getEnemies())
                .build();
    }


    @Builder
    public LastSavedHistory(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static LastSavedHistory error(){
        return LastSavedHistory.builder()
                .errorMessage("there has some problem")
                .build();

    }
}