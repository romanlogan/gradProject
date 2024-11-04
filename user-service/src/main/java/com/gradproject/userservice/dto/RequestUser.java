package com.gradproject.userservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Not an email format")
    private String email;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, message = "Name cannot be less than two characters")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters")
    private String pwd;

    public RequestUser() {
    }

    public RequestUser(String email, String name, String pwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }
}
