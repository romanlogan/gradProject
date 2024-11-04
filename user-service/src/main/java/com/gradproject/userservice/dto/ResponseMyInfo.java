package com.gradproject.userservice.dto;

import com.gradproject.userservice.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMyInfo {

    private String email;

    private String name;

    private ResponseHistory history;

    public ResponseMyInfo() {
    }

    @Builder
    public ResponseMyInfo(String email, String name, String userId, ResponseHistory history) {
        this.email = email;
        this.name = name;
        this.history = history;
    }

    public static ResponseMyInfo create(UserEntity user, ResponseHistory history) {

        return ResponseMyInfo.builder()
                .email(user.getEmail())
                .name(user.getName())
                .history(history)
                .build();
    }
}
