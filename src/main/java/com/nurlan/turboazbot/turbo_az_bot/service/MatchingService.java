package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddCustomRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CriteriaDataRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.NotificationLogRepo;
import com.nurlan.turboazbot.turbo_az_bot.telegram.TurboAzBot;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class MatchingService {
    private final CarAddRepo carAddRepo;
    private final CriteriaDataRepo criteriaRepo;
    private final NotificationService notificationService;
    private final NotificationLogRepo notification;
    private final CarAddCustomRepo customRepo;
    private final TurboAzBot bot;


    public MatchingService(CarAddRepo carAddRepo, CriteriaDataRepo criteriaRepo, NotificationService notificationService, NotificationLogRepo notification, CarAddCustomRepo customRepo, TurboAzBot bot) {
        this.carAddRepo = carAddRepo;
        this.criteriaRepo = criteriaRepo;
        this.notificationService = notificationService;
        this.notification = notification;
        this.customRepo = customRepo;
        this.bot = bot;
    }

    @Transactional
    public void criteriaWithCars() {
        List<SearchCriteria> Allcriteria = criteriaRepo.findAll();
        for (SearchCriteria criteria : Allcriteria) {
            String title = criteria.getTitle();
            Integer yearTo = criteria.getYearTo();
            Integer yearFrom = criteria.getYearFrom();
            Integer priceTo = criteria.getPriceTo();
            Integer priceFrom = criteria.getPriceFrom();
            LocalTime createdAtTo = criteria.getCreatedAtTo();
            LocalTime createdAtFrom = criteria.getCreatedAtFrom();

            List<CarAd> cars = customRepo.searchCarAds(title,yearFrom,yearTo,priceFrom,priceTo,createdAtFrom,createdAtTo);
            for (CarAd car : cars) {
                car.getSearchCriteria().add(criteria);

            }
            User user = criteria.getUser();

            boolean alreadySent = notification.existsByUserAndAnyCarIn(user, cars);
            if (!alreadySent) {
                notificationService.sendCarNotification(user, cars);
            }


        }


    }
}
