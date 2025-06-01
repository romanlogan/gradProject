package com.gradproject.gameservice.dto;

import com.gradproject.gameservice.constant.ExitType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class SaveCardGameRequest {

    private Integer gameId;
    private String playerEmail;
    private LocalDateTime startTime;
    private ExitType exitType;
    private Integer hp;
    private String cards;
    private String route;
    private Map<String,Integer> enemy = new HashMap<>();

    public SaveCardGameRequest() {
    }


    @Builder
    public SaveCardGameRequest(Integer gameId, String playerEmail, LocalDateTime startTime, ExitType exitType, Integer hp, String cards, String route, Map<String, Integer> enemy) {
        this.gameId = gameId;
        this.playerEmail = playerEmail;
        this.startTime = startTime;
        this.exitType = exitType;
        this.hp = hp;
        this.cards = cards;
        this.route = route;
        this.enemy = enemy;
    }

    public static SaveCardGameRequest create(LinkedHashMap map) {

        ArrayList list = (ArrayList) map.get("startTime");

        LocalDateTime localDateTime = LocalDateTime.of((Integer) list.get(0),
                (Integer) list.get(1),
                (Integer) list.get(2),
                (Integer) list.get(3),
                (Integer) list.get(4),
                (Integer) list.get(5),
                (Integer) list.get(6)
        );

        return SaveCardGameRequest.builder()
                .gameId((Integer) map.get("gameId"))
                .playerEmail((String) map.get("playerEmail"))
                .startTime(localDateTime)
                .exitType(ExitType.valueOf((String) map.get("exitType")))
                .hp((Integer) map.get("hp"))
                .cards((String) map.get("cards"))
                .route((String) map.get("route"))
                .enemy((Map<String, Integer>) map.get("enemy"))
                .build();

    }
}
