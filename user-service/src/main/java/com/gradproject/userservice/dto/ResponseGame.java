package com.gradproject.userservice.dto;

import com.gradproject.userservice.constant.ExitType;
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


}
