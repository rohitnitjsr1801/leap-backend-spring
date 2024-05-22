package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.PurchaseHistory;
import com.leapbackend.spring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByProductId(Long productId);

    List<PurchaseHistory> findByCustomer(User customer);

    @Query("SELECT COUNT(ph) FROM PurchaseHistory ph WHERE ph.product.manager.id = :managerId AND ph.purchaseDate <= (SELECT MIN(p.startDate) FROM Promotion p WHERE p.manager.id = :managerId)")
    int countInteractionsBeforePromotion(Long managerId);
}
