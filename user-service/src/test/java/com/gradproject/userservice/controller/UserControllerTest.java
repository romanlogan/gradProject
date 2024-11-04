package com.gradproject.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.userservice.dto.RequestLogin;
import com.gradproject.userservice.dto.RequestUser;
import com.gradproject.userservice.entity.UserEntity;
import com.gradproject.userservice.exception.NoMatchPasswordException;
import com.gradproject.userservice.repository.UserRepository;
import com.gradproject.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(controllers = UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("create new user")
    void createUser() throws Exception {

        RequestUser user = new RequestUser("test@test.com", "test", "12341234");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("email cannot be blank, when create new user ")
    void createUserWithBlankEmail() throws Exception {

        RequestUser user = new RequestUser(" ", "test", "12341234");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.email.message").value("Not an email format"))
                .andExpect(jsonPath("$.errorMap.email.rejectedValue").value(" "));
    }

    @Test
    @DisplayName("name cannot be blank, when create new user ")
    void createUserWithBlankName() throws Exception {

        RequestUser user = new RequestUser("test@test.com", " ", "12341234");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.name.message").value("Name cannot be blank"))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value(" "));
    }

    @Test
    @DisplayName("name cannot be one character, when create new user ")
    void createUserWithOneCharacterName() throws Exception {

        RequestUser user = new RequestUser("test@test.com", "a", "12341234");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.name.message").value("Name cannot be less than two characters"))
                .andExpect(jsonPath("$.errorMap.name.rejectedValue").value("a"));
    }

    @Test
    @DisplayName("password cannot be blank, when create new user ")
    void createUserWithBlankPassword() throws Exception {

        RequestUser user = new RequestUser("test@test.com", "test@test.com", " ");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                //2개 중 한개가 랜덤으로 들어가나 ?
                .andExpect(jsonPath("$.errorMap.pwd.message").value("Password must be equal or grater than 8 characters"))
//                .andExpect(jsonPath("$.errorMap.pwd.message").value("Password must be equal or grater than 8 characters"))
                .andExpect(jsonPath("$.errorMap.pwd.rejectedValue").value(" "));
    }




//    로그인 관련 핸들링 다시 공부 할것

//    401 에러가 뜨는 이유는 ?
    @Test
    @DisplayName("login will be successful, when send correct password")
    void login() throws Exception{

        userRepository.save(new UserEntity(
                "test@test.com",
                "test",
                UUID.randomUUID().toString(),
                passwordEncoder.encode("1234")));

        String email = "test@test.com";
        String password = "1234";
        RequestLogin request = new RequestLogin(email, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("if send wrong email, will return ")
    void login2() throws Exception{

        userRepository.save(new UserEntity(
                "test@test.com",
                "test",
                UUID.randomUUID().toString(),
                passwordEncoder.encode("1234")));

        String email = "test22@test.com";
        String password = "1234";
        RequestLogin request = new RequestLogin(email, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("if send correct email with not correct password, will return ")
    void login3() throws Exception{

        UserEntity userEntity = new UserEntity(
                "test@test.com",
                "test",
                UUID.randomUUID().toString(),
                passwordEncoder.encode("1234"));

        UserEntity returnedUser = userRepository.save(userEntity);

        UserEntity user = userRepository.findByEmail("test@test.com");

        String email = "test@test.com";
        String password = "12345678";
        RequestLogin request = new RequestLogin(email, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}