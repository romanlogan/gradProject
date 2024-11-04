package com.gradproject.historyservice.repository;

import com.gradproject.historyservice.constant.ExitType;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.entity.CardGameHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class HistoryRepositoryCustomImplTest {

    @Autowired
    private HistoryRepository historyRepository;

    @BeforeEach
    void beforeEach() {

//        6/20 8:00:30 <- 6/21 5:30:30 <- 7/20  5:40:30 <- 7/20 6:30:30


        Map map = new HashMap();

        historyRepository.save(new CardGameHistory(
                        LocalDateTime.of(2024, 6, 20, 5, 30, 30),
                        LocalDateTime.of(2024, 6, 20, 8, 0, 30),
                        ExitType.PAUSE,
                        1,
                        "asdf@asdf.com",
                        100,
                        "31,32,33,345:40 30,35",
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
                        "asdf@asdf.com",
                        100,
                        "31,32,33,34,35",
                        "0,1,2,4,6,7,9",
                        map
                )
        );      //1:00

    }

    @Test
    void getLastSavedHistory() {

        CardGameHistory history = historyRepository.getLastSavedHistory(1, "asdf@asdf.com");


        assertThat(history.getEndTime()).isEqualTo(LocalDateTime.of(2024, 7, 20, 6, 30, 30));

    }


}










