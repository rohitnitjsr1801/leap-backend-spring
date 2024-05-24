package com.leapbackend.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization_analytics")
@Getter
@Setter
public class OrganizationAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "total_revenue")
    private double totalRevenue;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
