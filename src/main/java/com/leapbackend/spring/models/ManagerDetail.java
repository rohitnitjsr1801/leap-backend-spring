package com.leapbackend.spring.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manager_details")
@Getter
@Setter
public class ManagerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "manager")
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "manager")
    private List<Product> products;

    private String Organization;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
