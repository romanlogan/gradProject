package com.gradproject.historyservice.entity;


import com.gradproject.historyservice.constant.ExitType;
import com.gradproject.historyservice.dto.SaveCardGameRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
//@Table(name = "card_game_history")
@Getter
@Setter
public class CardGameHistory extends History{


    private Integer hp;
    private String cards;
    private String route;       //마지막 지점이 저장시에 플레이 했던 지점

    @ElementCollection
    @CollectionTable(
            name = "enemy",
            joinColumns = @JoinColumn(name="history_id")
    )
    @MapKeyColumn(name = "enemy_type")
    @Column(name = "enemy_quantity")
    private Map<String,Integer> enemies = new HashMap<>();

    public CardGameHistory() {

    }

    @Builder
    public CardGameHistory(LocalDateTime startTime, LocalDateTime endTime, ExitType exitType, Integer gameId, String userEmail, Integer hp, String cards, String route, Map<String, Integer> enemy) {
        super.setStartTime(startTime);
        super.setEndTime(endTime);
        super.setExitType(exitType);
        super.setGameId(gameId);
        super.setUserEmail(userEmail);
        this.hp = hp;
        this.cards = cards;
        this.route = route;
        this.enemies = enemy;
    }

    public static CardGameHistory create(SaveCardGameRequest request) {

        return CardGameHistory.builder()
                .startTime(request.getStartTime())
                .endTime(LocalDateTime.now())
                .exitType(request.getExitType())
                .gameId(request.getGameId())
                .userEmail(request.getPlayerEmail())
                .hp(request.getHp())
                .cards(request.getCards())
                .route(request.getRoute())
                .enemy(request.getEnemy())
                .build();
    }


}
