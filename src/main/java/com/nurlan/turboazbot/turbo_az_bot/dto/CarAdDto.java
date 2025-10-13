package com.nurlan.turboazbot.turbo_az_bot.dto;

import com.nurlan.turboazbot.turbo_az_bot.entity.SearchCriteria;
import com.nurlan.turboazbot.turbo_az_bot.entity.User;
import lombok.Data;

@Data
public class CarAdDto {

    private Integer id;

    private String title;

    private String year;

    private String price;

    private String createdAt;

    private String link;

    private String externalId;

    private User user;

    private SearchCriteria searchCriteria;

}
