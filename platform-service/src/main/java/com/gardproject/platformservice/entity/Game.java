package com.gardproject.platformservice.entity;

import com.gardproject.platformservice.constant.Genre;
import com.gardproject.platformservice.constant.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.print.DocFlavor;

//설치해야 하는 게임이나 돈을 주고 사야하는 게임, 콘솔 게임등의 체험판같은 소스들을 올려서 유저들이 체험할 수 있고 좀더 구매력을 늘릴 수 있다

@Entity
@Table(name = "game")
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private String title;
//    private int price;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "img_url")
    private String imgUrl;

    public Game() {
    }

    @Builder
    private Game(String title, String description, Status status, Genre genre,String imgUrl) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.genre = genre;
        this.imgUrl = imgUrl;
    }

    public static Game create(String title, String description, Status status, Genre genre, String imgUrl){

        return Game.builder()
                .title(title)
                .description(description)
                .status(status)
                .genre(genre)
                .imgUrl(imgUrl)
                .build();
    }
}
