package com.gardproject.platformservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlatformMainResponse {

    List<PlatformGameDto> platformGameDtoList;

    public PlatformMainResponse() {
    }

    @Builder
    public PlatformMainResponse(List<PlatformGameDto> platformGameDtoList) {
        this.platformGameDtoList = platformGameDtoList;
    }

    public static PlatformMainResponse create(List<PlatformGameDto> platformMainGameList) {

        return PlatformMainResponse.builder()
                .platformGameDtoList(platformMainGameList)
                .build();

    }

}
