package com.nurlan.turboazbot.turbo_az_bot.service;


import com.nurlan.turboazbot.turbo_az_bot.entity.CarAd;
import com.nurlan.turboazbot.turbo_az_bot.entity.NotificationLog;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import com.nurlan.turboazbot.turbo_az_bot.repo.NotificationLogRepo;
import com.nurlan.turboazbot.turbo_az_bot.telegram.TurboAzBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

   private final TurboAzBot bot;
   private final NotificationLogRepo notificationLogRepo;

    public NotificationService(TurboAzBot bot, NotificationLogRepo notificationLogRepo) {
        this.bot = bot;
        this.notificationLogRepo = notificationLogRepo;
    }


    public void sendCarNotification(User user, List<CarAd> car){

        int count =0;
        bot.sendMessage(user.getTelegramChatId(), "tebrikler! evvel daxil etdiyiniz criterianiza uygun  elanlar tapildi");

        for(CarAd carAd : car) {
            count++;

            bot.sendMessage(user.getTelegramChatId(),""+carAd.getLink());



        }

        NotificationLog notificationLog = new NotificationLog();

        notificationLog.setUser(user);
        notificationLog.setCars(car);
        notificationLog.setSentAt(LocalDateTime.now());
        notificationLogRepo.save(notificationLog);



    }
}
