package com.gradproject.gameservice.entity;

import com.gradproject.gameservice.constant.ExitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Table(name="history")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ExitType exitType;

    private Integer gameId;

    private String userEmail;


    public History() {
    }

}
