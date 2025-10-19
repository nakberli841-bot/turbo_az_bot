package com.nurlan.turboazbot.turbo_az_bot.dto;

import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class CarAdDto {

    private Integer id;

    private String title;

    private Integer year;

    private Integer price;

    private LocalTime createdAt;

    private String link;

    private String externalId;

    private User user;

    private List<SearchCriteria> searchCriteria;

}
