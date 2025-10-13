package com.nurlan.turboazbot.turbo_az_bot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class CarAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String year;

    private String price;

    private String createdAt;

    private String link;

    @Column(unique = true)
    private String externalId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "search_criteria_id")
    @JsonIgnore
    private SearchCriteria searchCriteria;



}
