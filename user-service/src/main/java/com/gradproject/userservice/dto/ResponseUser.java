package com.gradproject.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)      //사용자 조회 2번 영상 보기
public class ResponseUser {

    private String email;
    private String name;
    private String userId;

}
