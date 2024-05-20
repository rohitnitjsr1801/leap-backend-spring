package com.leapbackend.spring.models;

import com.leapbackend.spring.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customerDetails")
public class CustomerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "InterestedCustomers", cascade = CascadeType.ALL)
    private List<Promotion> InterestedPromotions;

    @ManyToMany(mappedBy = "BoughtCustomers", cascade = CascadeType.ALL)
    private List<Promotion> BoughtPromotions;

    private Gender gender;

    private int age;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
