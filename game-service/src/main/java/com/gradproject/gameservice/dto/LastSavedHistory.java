package com.gradproject.gameservice.dto;

import com.gradproject.gameservice.constant.ExitType;
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
    public LastSavedHistory(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static LastSavedHistory error(){
        return LastSavedHistory.builder()
                .errorMessage("문제가 발생하였습니다.")
                .build();

    }
}