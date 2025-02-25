package com.gardproject.platformservice.controller;

import com.gardproject.platformservice.dto.PlatformMainResponse;
import com.gardproject.platformservice.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlatformController {

    private PlatformService platformService;

    @Autowired
    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/")
    public String main(Model model) {

        PlatformMainResponse response = platformService.getPlatformMain();

        model.addAttribute("response", response);
        return "platform-main";
    }
}
