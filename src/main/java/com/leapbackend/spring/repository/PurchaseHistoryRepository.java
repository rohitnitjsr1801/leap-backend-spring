package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.PurchaseHistory;
import com.leapbackend.spring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByProductId(Long productId);

    List<PurchaseHistory> findByCustomer(User customer);
}
