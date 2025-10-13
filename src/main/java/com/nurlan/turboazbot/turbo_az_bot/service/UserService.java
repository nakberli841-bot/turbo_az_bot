package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CriteriaDataRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.UserDataRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDataRepo userRepo;
    private final CarAddRepo carAddRepo;
    private final CriteriaDataRepo criteriaRepo;
    public UserService(UserDataRepo userRepo, CarAddRepo carAddRepo, CriteriaDataRepo criteriaRepo) {
        this.userRepo = userRepo;
        this.carAddRepo = carAddRepo;
        this.criteriaRepo = criteriaRepo;
    }


    public List<CarAd> creatUserAndCriteria(User user, SearchCriteria criteria) {
        String title = criteria.getTitle();
        String year = criteria.getYear();
        String price = criteria.getPrice();
        String createdAt = criteria.getCreatedAt();

        List<CarAd> cars = carAddRepo.findByCars(title, year, price, createdAt);
        for (CarAd car : cars) {
            car.setSearchCriteria(criteria);
        }
        criteriaRepo.save(criteria);

        User userr= userRepo.findByEmail(user.getEmail());
        if(userr == null) {
            criteria.setUser(user);
            userRepo.save(user);
        }else {
            criteria.setUser(userr);
            userRepo.save(userr);
        }
        return cars;

    }
}
