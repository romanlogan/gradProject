package com.gradproject.userservice.service;

import com.gradproject.userservice.dto.UserDto;
import com.gradproject.userservice.entity.UserEntity;
import com.gradproject.userservice.exception.DuplicateEmailException;
import com.gradproject.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("create user")
    void createUser() {

        UserDto userDto = new UserDto("test@test.com", "test", "1234");
        userService.createUser(userDto);

        UserEntity user = userRepository.findByEmail("test@test.com");
        assertThat(user.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("throw DuplicateEmailException when apply for registration with an existing email address,")
    void duplicateEmailRegistration() {

        UserDto userDto = new UserDto("test@test.com", "test", "1234");
        userService.createUser(userDto);

        assertThatThrownBy(() -> {
            userService.createUser(userDto);
        }).isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("This email address is already registered");
    }

}