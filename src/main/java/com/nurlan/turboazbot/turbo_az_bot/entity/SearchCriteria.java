package com.nurlan.turboazbot.turbo_az_bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "search_criteria")
public class SearchCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private Integer yearTo;
    private Integer yearFrom;
    private Integer PriceTo;
    private Integer PriceFrom;
    private LocalTime createdAtTo;
    private LocalTime createdAtFrom;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "searchCriteria")
    private List<CarAd> carAds = new ArrayList<>();


}
