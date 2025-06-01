package com.gradproject.gameservice.dto;

import com.gradproject.gameservice.constant.ExitType;
import com.gradproject.gameservice.entity.CardGame;
import com.gradproject.gameservice.entity.Game;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class ResponseGame {

    private Long id;
    private Integer myHp;
    private Integer enemyHp;
    private Integer money;
    private String playerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExitType exitType;


    @Builder
    public ResponseGame(Long id, Integer myHp, Integer enemyHp, Integer money, String playerEmail, LocalDateTime startTime, LocalDateTime endTime, ExitType exitType) {
        this.id = id;
        this.myHp = myHp;
        this.enemyHp = enemyHp;
        this.money = money;
        this.playerEmail = playerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exitType = exitType;
    }

    public static ResponseGame create(CardGame game) {

        return ResponseGame.builder()
                .id(game.getId())
                .myHp(game.getMyHp())
                .enemyHp(game.getEnemyHp())
                .money(game.getMoney())
                .playerEmail(game.getPlayerEmail())
                .build();
    }
}
