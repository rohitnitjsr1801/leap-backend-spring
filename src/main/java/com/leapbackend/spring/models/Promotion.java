package com.leapbackend.spring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leapbackend.spring.enums.AgeRange;
import com.leapbackend.spring.enums.Gender;
import com.leapbackend.spring.enums.PurchaseFrequency;
import com.leapbackend.spring.enums.promotionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Gender gender;

    private AgeRange ageRange;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    private PurchaseFrequency purchaseFrequency;

    private String promotionType;

    private double discountRate;

    private LocalDate startDate;

    private LocalDate endDate;

    private int interestedCount;

    private int buyCount;

    private promotionStatus promotionstatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
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
