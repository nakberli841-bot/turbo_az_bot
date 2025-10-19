package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDataRepo userRepo;
    private final CarAddRepo carAddRepo;
    private final CriteriaDataRepo criteriaRepo;
    private final CarAddCustomRepo customRepo;
    private final NotificationLogRepo notification;
    private final NotificationService notificationService;





    @Transactional
    public List<CarAd> creatUserAndCriteria(User user, List<SearchCriteria> criteria) {

        User userToAssociate;

        User userDB = userRepo.findByEmail(user.getEmail());
        if(userDB == null) {

           userToAssociate= userRepo.save(user);
        }else {
            userToAssociate = userDB;
        }


        List<CarAd> allCars = new ArrayList<>();

        for (SearchCriteria sc : criteria) {
            String title = sc.getTitle();
            Integer yearTo = sc.getYearTo();
            Integer yearFrom = sc.getYearFrom();
            Integer priceTo = sc.getPriceTo();
            Integer priceFrom = sc.getPriceFrom();
            LocalTime createdAtTo = sc.getCreatedAtTo();
            LocalTime createdAtFrom = sc.getCreatedAtFrom();

            criteriaRepo.save(sc);

            sc.setUser(userToAssociate);



            List<CarAd> cars = customRepo.searchCarAds(title,yearFrom,yearTo,priceFrom,priceTo,createdAtFrom,createdAtTo);
            for (CarAd car : cars) {
                car.getSearchCriteria().add(sc);
                allCars.add(car);
            }


        }
        return allCars;
    }

}