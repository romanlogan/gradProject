package com.gradproject.userservice.entity;


import com.gradproject.userservice.dto.UserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Data
@Entity
@Table(name = "userEntity")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPwd;

    public UserEntity() {
    }

    @Builder
    public UserEntity(String email, String name, String userId, String encryptedPwd) {
        this.email = email;
        this.name = name;
        this.userId = userId;
        this.encryptedPwd = encryptedPwd;
    }

    public static UserEntity create(UserDto userDto, BCryptPasswordEncoder passwordEncoder) {

        return UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .userId(userDto.getUserId())
//                .encryptedPwd(userDto.getEncryptedPwd())
                .encryptedPwd(passwordEncoder.encode(userDto.getPwd()))
                .build();
    }
}
