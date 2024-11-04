package com.gradproject.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.commentservice.dto.RequestSaveComment;
import com.gradproject.commentservice.util.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("save comment")
    void createUser() throws Exception {

        RequestSaveComment request = new RequestSaveComment(1, "content1");

        // Mock Token 생성
        String token = getToken();
//        String token = "BearereyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.sjnGgorV5Yv12ymk_8fNkvxJmGmE-nHR0Cao87UdGBS9WYMrT91rqFWhXFD0NN2zPIWP3cPui04r1Ycn2R6maw";

        mockMvc.perform(MockMvcRequestBuilders.post("/save")
                        .header(HttpHeaders.AUTHORIZATION, token)  // 토큰 헤더 추가
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    private static String getToken() {
        return JwtTokenUtil.generateMockToken("test@example.com");
    }


    @Test
    @DisplayName("When save comment, content cannot be blank")
    void createUserWithBlankName() throws Exception {

        RequestSaveComment request = new RequestSaveComment(1, "");
        String token = getToken();


        mockMvc.perform(MockMvcRequestBuilders.post("/save")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("content cannot be blank"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value(""));
    }


    @Test
    @DisplayName("When save comment, Comments cannot exceed 255 characters")
    void createCommentWithExceed255Char() throws Exception {

        RequestSaveComment request = new RequestSaveComment(1, "Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque.Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque.Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque.");
        String token = getToken();


        mockMvc.perform(MockMvcRequestBuilders.post("/save")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("Please write your comment between 1 and 255 characters."))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value("Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque.Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque.Lorem ipsum odor amet, consectetuer adipiscing elit. Pulvinar conubia at placerat lacinia nisl curae magnis sapien rhoncus pulvinar donec rutrum placerat dolor rutrum neque sollicitudin dignissim cras turpis neque senectus penatibus ridiculus cras sodales habitasse elit tempus mi mollis maximus dapibus turpis nullam sollicitudin semper consectetur tellus sem convallis et vehicula turpis pharetra a nibh sed ligula risus a nullam potenti egestas enim bibendum conubia vehicula vivamus eros semper lobortis condimentum tempus in commodo condimentum tristique fames pretium nec quis mus quis auctor curabitur auctor consectetur odio senectus amet montes senectus convallis duis velit sem viverra vulputate pellentesque aptent congue libero molestie primis quisque sagittis ante vestibulum rutrum euismod id ullamcorper pellentesque."));
    }


}