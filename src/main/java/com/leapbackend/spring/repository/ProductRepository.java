package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
