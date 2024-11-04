package com.gradproject.commentservice.dto;


import lombok.Data;

@Data
public class RequestUser {

    private String email;
    private String name;
    private String pwd;

    public RequestUser() {
    }

    public RequestUser(String email, String name, String pwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }
}
