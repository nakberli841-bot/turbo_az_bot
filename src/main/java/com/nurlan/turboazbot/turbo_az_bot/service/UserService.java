package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CriteriaDataRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.UserDataRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    @Transactional
    public List<CarAd> creatUserAndCriteria(User user, List<SearchCriteria> criteria) {

        List<CarAd> allCars = new ArrayList<>();
        for (SearchCriteria sc : criteria) {
            String title = sc.getTitle();
            String year = sc.getYear();
            String price = sc.getPrice();
            String createdAt = sc.getCreatedAt();


            List<CarAd> cars = carAddRepo.findByCars(title, year, price, createdAt);
            for (CarAd car : cars) {
                car.getSearchCriteria().add(sc);
                allCars.add(car);
            }
            criteriaRepo.save(sc);

            User userr = userRepo.findByEmail(user.getEmail());
            if (userr == null) {
                sc.setUser(user);
                userRepo.save(user);
            }
        }
        return allCars;
    }

}