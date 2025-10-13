package com.nurlan.turboazbot.turbo_az_bot.controller;


import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping("/resultCars")
    public List<CarAd> resultCars(@RequestParam Integer userId) {
        List<CarAd>  cars= resultService.resultCarByUserId(userId);
        return cars;
    }

}
