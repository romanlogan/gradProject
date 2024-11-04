package com.gradproject.historyservice.service;

import com.gradproject.historyservice.constant.ExitType;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.dto.ResponseHistory;
import com.gradproject.historyservice.entity.CardGameHistory;
import com.gradproject.historyservice.entity.History;
import com.gradproject.historyservice.exception.LastSaveHistoryNotExistException;
import com.gradproject.historyservice.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class HistoryServiceImplTest {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;

    @BeforeEach
    void beforeEach() {

        Map map = new HashMap();

        historyRepository.save(new CardGameHistory(
                LocalDateTime.of(2024, 6, 20, 5, 30, 30),
                LocalDateTime.of(2024, 6, 20, 8, 0, 30),
                ExitType.PAUSE,
                1,
                "asdf@asdf.com",
                100,
                "31,32,33,34,35",
                "0,1,2,4,6,7,9",
                map
                )
        );      //2:30 - 150

        historyRepository.save(new CardGameHistory(
                LocalDateTime.of(2024, 7, 20, 5, 30, 30),
                LocalDateTime.of(2024, 7, 20, 5, 40, 30),
                ExitType.FINISH,
                1,
                "asdf@asdf.com",
                100,
                "31,32,33,34,35",
                "0,1,2,4,6,7,9",
                map)
        );      //0:10

        historyRepository.save(new CardGameHistory(
                LocalDateTime.of(2024, 6, 20, 5, 30, 30),
                LocalDateTime.of(2024, 6, 21, 5, 30, 30),
                ExitType.PAUSE,
                1,
                "asdf@asdf.com",
                100,
                "31,32,33,34,35",
                "0,1,2,4,6,7,9",
                map
                )
        );      //24:00 - 1440

        historyRepository.save(new CardGameHistory(
                LocalDateTime.of(2024, 7, 20, 5, 30, 30),
                LocalDateTime.of(2024, 7, 20, 6, 30, 30),
                ExitType.FINISH,
                1,
                "qwer@qwer.com",
                100,
                "31,32,33,34,35",
                "0,1,2,4,6,7,9",
                map
                )
        );      //1:00

    }

    @Test
    @DisplayName("Get a list of user's recently played games and information about them.")
    void getPlayedGameList() {

        //given
        String email = "asdf@asdf.com";

        //when
        ResponseHistory response = historyService.getPlayedGameList(email);

        //then
        assertThat(response.getPlayedGameInfoMap().get(1L).getLastFinishPlayingDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 20, 5, 40, 30));
        assertThat(response.getPlayedGameInfoMap().get(1L).getTotalPlayedTimeByMin()).isEqualTo(1600);
    }

    @Test
    @DisplayName("Get a list of user's recently played games and information about them.")
    void getPlayedGameList2() {

        //given
        String email = "qwer@qwer.com";

        //when
        ResponseHistory response = historyService.getPlayedGameList(email);

        //then
        assertThat(response.getPlayedGameInfoMap().get(1L).getLastFinishPlayingDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 20, 6, 30, 30));
        assertThat(response.getPlayedGameInfoMap().get(1L).getTotalPlayedTimeByMin()).isEqualTo(60);
    }

    @Test
    @DisplayName("Get the most recently saved game history")
    void getLastSavedHistory() {

        //given
        String email = "asdf@asdf.com";

        //when
        LastSavedHistory lastSavedHistory = historyService.getLastSavedHistory(1, email);

        //then
        assertThat(lastSavedHistory.getEndTime()).isEqualTo(LocalDateTime.of(2024, 6, 21, 5, 30, 30));
    }

    @Test
    @DisplayName("receive a request when there is no recent play history,then throw LastSaveHistoryNotExistException")
    void getLastSavedHistoryWithNoRecentHistory() {

        //given
        String email = "test@test.com";

        //when
        assertThatThrownBy(() -> {
            historyService.getLastSavedHistory(1, email);
        }).isInstanceOf(LastSaveHistoryNotExistException.class)
                .hasMessageContaining("최근 플레이한 기록이 없습니다.");
    }
}