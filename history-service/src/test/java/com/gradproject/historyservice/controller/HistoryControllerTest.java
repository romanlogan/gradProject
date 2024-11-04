package com.gradproject.historyservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.historyservice.constant.ExitType;
import com.gradproject.historyservice.dto.LastSavedHistory;
import com.gradproject.historyservice.dto.SaveCardGameRequest;
import com.gradproject.historyservice.exception.LastSaveHistoryNotExistException;
import com.gradproject.historyservice.service.HistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class HistoryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HistoryService historyService;


    @Test
    @DisplayName("save history")
    void save() throws Exception {

        Map map = new HashMap();
        map.put("dog",2);
        map.put("cat",1);
        SaveCardGameRequest request = new SaveCardGameRequest(1, "asdf@asdf.com", LocalDateTime.now(), ExitType.FINISH, 100, "31,32,33", "0,1,2,3", map);


        mockMvc.perform(MockMvcRequestBuilders.post("/save")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("return 200, when try to get the most recently saved game history")
    void getLastSavedHistory() throws Exception {

        LastSavedHistory response = new LastSavedHistory();

        Mockito.when(historyService.getLastSavedHistory(1,"asdf@asdf.com")).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/lastSavedHistory")
                        .param("gameId", "1")
                        .param("email", "asdf@asdf.com")

                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("return 200 with errorMessage, when try to get the most recently saved game history but it doesn't exist")
    void getLastSavedHistoryWithNotExistHistory() throws Exception {

        LastSavedHistory response = new LastSavedHistory();

        Mockito.when(historyService.getLastSavedHistory(1, "asdf@asdf.com")).thenThrow(new LastSaveHistoryNotExistException("최근 플레이한 기록이 없습니다."));

        mockMvc.perform(MockMvcRequestBuilders.get("/lastSavedHistory")
                        .param("gameId", "1")
                        .param("email", "asdf@asdf.com")

                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}