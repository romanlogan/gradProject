package com.gradproject.gameservice.dto;

import com.gradproject.gameservice.constant.ExitType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaveHistoryRequest {

//    private Integer gameId
    private Integer myHp;
    private Integer enemyHp;
    private Integer money;
    private String playerEmail;
    private LocalDateTime startTime;
    private ExitType exitType;

}
