package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CarAddRepo extends JpaRepository<CarAd,Integer>{

    Optional<CarAd>  findByExternalId(String externalId);


    @Query("SELECT c FROM CarAd c " +
            "WHERE (:title IS NULL OR c.title = :title) " +
            "AND (:year IS NULL OR c.year = :year) " +
            "AND (:price IS NULL OR c.price = :price) " +
            "AND (:createdAt IS NULL OR c.createdAt = :createdAt)")
    List<CarAd> findByCars(@Param("title") String title,
                               @Param("year") String year,
                               @Param("price") String price,
                               @Param("createdAt") String createdAt);



    @Query("SELECT c FROM CarAd c JOIN c.searchCriteria sc WHERE sc.id = :criteriaId")
    List<CarAd> findByCriteriaId(@Param("criteriaId") Integer criteriaId);


    @Query("SELECT c.externalId FROM CarAd c")
    List<String> findAllExternalIds();

}

