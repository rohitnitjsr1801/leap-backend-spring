package com.leapbackend.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promotion")
@Getter
@Setter
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gender;

    private String ageRange;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;


    private String purchaseFrequency;

    private String promotionType;

    private double discountRate;

    private LocalDate startDate;

    private LocalDate endDate;

    private int interestedCount;

    private int buyCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private ManagerDetail manager;

    @ManyToMany
    @JoinTable(
            name = "customer_interested_promotions",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_details_id")
    )
    private List<CustomerDetail> InterestedCustomers;

    @ManyToMany
    @JoinTable(
            name = "customer_bought_promotions",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_details_id")
    )
    private List<CustomerDetail> BoughtCustomers;
}
