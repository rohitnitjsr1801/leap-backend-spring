package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.CustomerDetail;
import com.leapbackend.spring.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail,Long> {
    Optional<CustomerDetail> findByUserId(Long userId);

    @Query("SELECT c.InterestedPromotions FROM CustomerDetail c WHERE c.id = :customerId")
    List<Promotion> findInterestedPromotionsByCustomerId(Long customerId);

    @Query("SELECT c.BoughtPromotions FROM CustomerDetail c WHERE c.id = :customerId")
    List<Promotion> findBoughtPromotionsByCustomerId(Long customerId);
}
