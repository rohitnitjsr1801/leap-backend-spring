package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Define custom query methods if needed
}
