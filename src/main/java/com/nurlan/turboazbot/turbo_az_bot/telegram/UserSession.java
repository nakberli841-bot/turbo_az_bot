package com.nurlan.turboazbot.turbo_az_bot.telegram;


import lombok.Data;

import java.time.LocalTime;

@Data
public class UserSession {
    public String name;
    public String email;
    public String title;
    public Integer yearTo;
    public Integer yearFrom;
    public Integer priceTo;
    public Integer priceFrom;
    public LocalTime createdAtTo;
    public LocalTime createdAtFrom;
    public int step = 0;
}