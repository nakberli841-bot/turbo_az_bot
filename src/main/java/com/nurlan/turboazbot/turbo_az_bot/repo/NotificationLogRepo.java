package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.NotificationLog;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepo extends JpaRepository<NotificationLog, Long> {

    @Query("""
SELECT COUNT(n) > 0 
FROM NotificationLog n 
JOIN n.cars c 
WHERE n.user = :user 
AND c IN :cars
""")
    boolean existsByUserAndAnyCarIn(@Param("user") User user, @Param("cars") List<CarAd> cars);}