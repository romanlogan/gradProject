package com.gradproject.userservice.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String decryptedPwd;

    private String encryptedPwd;

    public UserDto() {
    }

    @Builder
    public UserDto(String email, String name, String pwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }

    public static UserDto create(RequestUser request) {
        return UserDto.builder()
                .email(request.getEmail())
                .name(request.getName())
                .pwd(request.getPwd())
                .build();
    }

}
