package com.nurlan.turboazbot.turbo_az_bot.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class UserSession {

    @Id
    private Long telegramChatId;

    private String name;
    private String email;
    private String title;
    private Integer yearTo;
    private Integer yearFrom;
    private Integer priceTo;
    private Integer priceFrom;
    private LocalTime createdAtTo;
    private LocalTime createdAtFrom;
    private int step = 0;
}