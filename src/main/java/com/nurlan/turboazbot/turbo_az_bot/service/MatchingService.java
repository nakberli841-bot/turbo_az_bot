package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CriteriaDataRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {
    private final CarAddRepo carAddRepo;
   private final  CriteriaDataRepo criteriaRepo;

    public MatchingService(CarAddRepo carAddRepo, CriteriaDataRepo criteriaRepo) {
        this.carAddRepo = carAddRepo;
        this.criteriaRepo = criteriaRepo;
    }

    @Transactional
    public void criteriaWithCars() {
       List<SearchCriteria> Allcriteria= criteriaRepo.findAll();
       for (SearchCriteria criteria : Allcriteria) {
           String title = criteria.getTitle();
           String year = criteria.getYear();
           String price = criteria.getPrice();
           String createdAt = criteria.getCreatedAt();

           List<CarAd> cars = carAddRepo.findByCars(title, year, price, createdAt);
           for (CarAd car : cars) {
               car.getSearchCriteria().add(criteria);

           }

       }



    }
}
