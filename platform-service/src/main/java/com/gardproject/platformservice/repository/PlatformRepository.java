package com.gardproject.platformservice.repository;

import com.gardproject.platformservice.dto.PlatformGameDto;
import com.gardproject.platformservice.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Game, Long> {


    @Query("select new com.gardproject.platformservice.dto.PlatformGameDto(" +
            "game.id, game.title, game.description, game.status, game.genre, game.imgUrl" +
            ") " +
            "from Game game " +
            "order by game.id")
    List<PlatformGameDto> getPlatformMainGameList();

}
