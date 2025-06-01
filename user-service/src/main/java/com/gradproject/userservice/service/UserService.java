package com.gradproject.userservice.service;

import com.gradproject.userservice.dto.ResponseMyInfo;
import com.gradproject.userservice.dto.UserDto;
import com.gradproject.userservice.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

//public interface UserService extends UserDetailsService {
public interface UserService extends UserDetailsService {

    Long createUser(UserDto userDto);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);

    ResponseMyInfo getResponseMyInfo(String userEmail);

    ResponseMyInfo getResponseMyInfoByKafka(String topic, String email);
}
