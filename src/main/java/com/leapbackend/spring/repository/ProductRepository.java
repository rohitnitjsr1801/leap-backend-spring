package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.manager.id = :managerId")
    List<Product> findAllByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT p FROM Product p WHERE p.manager.Organization = :organization")
    List<Product> findProductsByOrganization(@Param("organization") String organization);
}
