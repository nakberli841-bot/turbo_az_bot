package com.nurlan.turboazbot.turbo_az_bot.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // Hər bildiriş bir user üçündür

    @ManyToMany
    @JoinTable(
            name = "notification_carad",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "car_ad_id")
    )
    private List<CarAd> cars = new ArrayList<>();

    private LocalDateTime sentAt;
}
