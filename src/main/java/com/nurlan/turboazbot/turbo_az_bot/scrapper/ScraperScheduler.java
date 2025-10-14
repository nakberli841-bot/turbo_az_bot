package com.nurlan.turboazbot.turbo_az_bot.scrapper;


import com.nurlan.turboazbot.turbo_az_bot.service.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final TurboAzScraper scraper;
    private final MatchingService matchingService;

    @Scheduled(fixedRate = 21600000)
    public void autoScrape() {
        try {
            log.info("autoscraping basladi");
            scraper.scrapeAndSave(1, 2);
            log.info("autoscraping tamamlandi");
            matchingService.criteriaWithCars();
        }
        catch (Exception e) {
            log.error(" autoscraping zamani xeta bas verdi  "+e.getMessage());
        }
    }
}
