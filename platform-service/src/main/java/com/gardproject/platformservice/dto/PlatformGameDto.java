package com.gardproject.platformservice.dto;

import com.gardproject.platformservice.constant.Genre;
import com.gardproject.platformservice.constant.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatformGameDto {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private Genre genre;

    private String imgUrl;

    public PlatformGameDto() {
    }

    public PlatformGameDto(Long id, String title, String description, Status status, Genre genre,String imgUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.genre = genre;
        this.imgUrl = imgUrl;
    }
}
