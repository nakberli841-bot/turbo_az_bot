package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface CarAddRepo extends JpaRepository<CarAd,Integer>{

    Optional<CarAd>  findByExternalId(String externalId);


    @Query("SELECT c FROM CarAd c " +
            "WHERE (:title IS NULL OR c.title = :title) " +
            "AND (:yearFrom IS NULL OR :yearTo IS NULL OR c.year >= :yearFrom) " +
            "AND (:yearTo IS NULL OR c.year <= :yearTo) " +
            "AND (:priceFrom IS NULL OR :priceTo IS NULL OR c.price >= :priceFrom) " +
            "AND (:priceTo IS NULL OR c.price <= :priceTo) " +
            "AND (:createdFrom IS NULL OR :createdTo IS NULL OR c.createdAt >= :createdFrom) " +
            "AND (:createdTo IS NULL OR c.createdAt <= :createdTo)")
    List<CarAd> findByCars(
            @Param("title") String title,
            @Param("yearFrom") Integer yearFrom,
            @Param("yearTo") Integer yearTo,
            @Param("priceFrom") Integer priceFrom,
            @Param("priceTo") Integer priceTo,
            @Param("createdFrom") LocalTime createdFrom,
            @Param("createdTo") LocalTime createdTo);

    @Query("SELECT c FROM CarAd c JOIN c.searchCriteria sc WHERE sc.id = :criteriaId")
    List<CarAd> findByCriteriaId(@Param("criteriaId") Integer criteriaId);


    @Query("SELECT c.externalId FROM CarAd c")
    List<String> findAllExternalIds();

}

