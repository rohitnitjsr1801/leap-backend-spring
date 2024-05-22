package com.leapbackend.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
@Getter
@Setter
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "promotion_id", nullable = false)
//    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private ManagerDetail manager;

    @Column(name = "conv_rate")
    private double convRate;

    @Column(name = "pre_revenue")
    private double preRevenue;

    @Column(name = "post_revenue")
    private double postRevenue;

    @Column(name = "pre_interactions")
    private int preInteractions;

    @Column(name = "post_interactions")
    private int postInteractions;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
