package com.gardproject.platformservice.service;

import com.gardproject.platformservice.dto.PlatformGameDto;
import com.gardproject.platformservice.dto.PlatformMainResponse;
import com.gardproject.platformservice.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformServiceImpl implements PlatformService {

    private PlatformRepository platformRepository;

    @Autowired
    public PlatformServiceImpl(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Override
    public PlatformMainResponse getPlatformMain() {

        List<PlatformGameDto> platformMainGameList = platformRepository.getPlatformMainGameList();
        return PlatformMainResponse.create(platformMainGameList);
    }
}
