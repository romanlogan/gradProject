package com.gardproject.platformservice.service;

import com.gardproject.platformservice.constant.Genre;
import com.gardproject.platformservice.constant.Status;
import com.gardproject.platformservice.dto.PlatformMainResponse;
import com.gardproject.platformservice.entity.Game;
import com.gardproject.platformservice.repository.PlatformRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PlatformServiceImplTest {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepository platformRepository;

    @Test
    @DisplayName("get platform main game list")
    void getPlatformMain() {

        Game game = Game.create("test-title", "test-desc", Status.OPEN, Genre.ACTION,"url");
        platformRepository.save(game);

        PlatformMainResponse response = platformService.getPlatformMain();

        assertThat(response.getPlatformGameDtoList()).hasSize(1);
        assertThat(response.getPlatformGameDtoList().get(0).getTitle()).isEqualTo("test-title");

    }
}