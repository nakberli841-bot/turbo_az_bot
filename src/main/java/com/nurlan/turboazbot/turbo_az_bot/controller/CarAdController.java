package com.nurlan.turboazbot.turbo_az_bot.controller;

import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDto;
import com.nurlan.turboazbot.turbo_az_bot.service.CarAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@RequiredArgsConstructor
public class CarAdController {
    private final CarAdService carAdService;



    @PostMapping("/update")
    public String updateFromTurboAz() {
        carAdService.updateDatabaseFromTurboAz();
        return "Database updated from Turbo.az!";
    }


    @GetMapping("/findCar")
    public List<CarAdDto> findCarAds(@RequestParam(required = false)String title,
                                     @RequestParam(required = false)String year,
                                     @RequestParam(required = false)String price,
                                     @RequestParam(required = false)String createdAt,
                                     @RequestParam(required = false)String link,
                                     @RequestParam(required = false)String externalId,
                                     @RequestParam(required = false)int page ,
                                     @RequestParam(required = false)int size)
    {
        return carAdService.find(title, year, price, createdAt, link, externalId, page, size);

    }



}
