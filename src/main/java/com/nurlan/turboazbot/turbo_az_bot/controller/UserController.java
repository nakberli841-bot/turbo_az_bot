package com.nurlan.turboazbot.turbo_az_bot.controller;

import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDto;
import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/creatUserAndCriteria")
    public List<CarAd> creatUser(@RequestBody CarAdDto carAdDto) {
      return   userService.creatUserAndCriteria(carAdDto.getUser(), carAdDto.getSearchCriteria());

    }
}
