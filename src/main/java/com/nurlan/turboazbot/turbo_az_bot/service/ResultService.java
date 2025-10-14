package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CriteriaDataRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {

    private final CriteriaDataRepo criteriaRepo;
    private final CarAddRepo carAddRepo;

    public ResultService(CriteriaDataRepo criteriaRepo, CarAddRepo carAddRepo) {
        this.criteriaRepo = criteriaRepo;
        this.carAddRepo = carAddRepo;
    }


    public List<CarAd> resultCarByUserId(Integer userId) {
        List<SearchCriteria> criterias = criteriaRepo.findByUserId(userId);

        List<CarAd> allCars = new ArrayList<CarAd>();

        for (SearchCriteria criteria : criterias) {
            List<CarAd> Cars = carAddRepo.findByCriteriaId(criteria.getId());
            allCars.addAll(Cars);
        }
        return allCars;

    }
}
