package com.gradproject.gameservice.entity;

import com.gradproject.gameservice.dto.SaveHistoryRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="card_game")
@Getter
@Setter
public class CardGame extends Game{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private Integer myHp;

    private Integer enemyHp;

    private Integer money;

    public CardGame() {
    }

    @Builder
    public CardGame(Integer myHp, Integer enemyHp, Integer money, String email) {
        super.setPlayerEmail(email);
        this.myHp = myHp;
        this.enemyHp = enemyHp;
        this.money = money;
    }

    public static CardGame create(SaveHistoryRequest request) {
        return CardGame.builder()
                .email(request.getPlayerEmail())
                .myHp(request.getMyHp())
                .enemyHp(request.getEnemyHp())
                .money(request.getMoney())
                .build();
    }
}
