package com.nurlan.turboazbot.turbo_az_bot.scrapper;


import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final TurboAzScraper scraper;
    private final MatchingService matchingService;
    private final CarAddRepo carAddRepo;

    @Scheduled(fixedRate = 900000)
    public void autoScrape() {
        try {
            log.info("autoscraping basladi");
            scraper.scrapeAndSave();
            log.info("autoscraping tamamlandi");
            matchingService.criteriaWithCars();

        }
        catch (Exception e) {
            log.error(" autoscraping zamani xeta bas verdi  "+e.getMessage());
        }
    }
}
