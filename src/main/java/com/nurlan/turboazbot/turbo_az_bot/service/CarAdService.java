package com.nurlan.turboazbot.turbo_az_bot.service;

import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDto;
import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDtoMapper;
import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddCustomRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.CarAddRepo;
import com.nurlan.turboazbot.turbo_az_bot.scrapper.TurboAzScraper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CarAdService {
    private final CarAddRepo carAddRepo;
    private final CarAddCustomRepo customCarAddRepo;
    private final TurboAzScraper turboAzScraper;
    private final CarAdDtoMapper mapper;
    private final MatchingService matchingService;

    public CarAdService(CarAddRepo carAddRepo, CarAddCustomRepo customCarAddRepo, TurboAzScraper turboAzScraper, CarAdDtoMapper carAdDtoMapper, MatchingService matchingService) {
        this.carAddRepo = carAddRepo;
        this.customCarAddRepo = customCarAddRepo;
        this.turboAzScraper = turboAzScraper;
        this.mapper = carAdDtoMapper;
        this.matchingService = matchingService;
    }


@Transactional//bunu mutleq niye bura yazdis onun sebebii arasdir
    public  void updateDatabaseFromTurboAz() {
       try{

           List<CarAd> entities = turboAzScraper.scrapeAndSave(1,3)
                   .stream()
                   .map(CarAdDDto -> mapper.toEntity(CarAdDDto))
                   .toList();
        carAddRepo.saveAll(entities);

        matchingService.criteriaWithCars();




       }
       catch(Exception e) {
           e.printStackTrace();
       }

    }



    public List<CarAdDto> find( String title,
                                String year,
                                String price,
                                String createdAt,
                                String link,
                                String externalId,
                                int page,
                                int size){
        return customCarAddRepo.searchCarAds(title,year,price,createdAt,link,externalId,page,size).stream().map(CarAdEntity->mapper.toDto(CarAdEntity)).toList();
    }
}
