package com.gradproject.gameservice.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
public class Game {

    private String playerEmail;

}
