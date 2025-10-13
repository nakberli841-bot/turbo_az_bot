package com.nurlan.turboazbot.turbo_az_bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private String year;
    private String Price;
    private String createdAt;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "searchCriteria", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<CarAd> carAds = new ArrayList<>();


}
