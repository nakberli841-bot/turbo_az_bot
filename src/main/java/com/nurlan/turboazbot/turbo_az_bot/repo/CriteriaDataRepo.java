package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CriteriaDataRepo extends JpaRepository<SearchCriteria, Integer> {

    @Query(value = "SELECT * FROM search_criteria WHERE user_id = :userId", nativeQuery = true)
    List<SearchCriteria> findByUserId(@Param("userId") Integer userId);
}
