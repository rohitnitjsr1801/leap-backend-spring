package com.leapbackend.spring.repository;

import com.leapbackend.spring.enums.promotionStatus;
import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByManager(ManagerDetail ownerDetail);

    @Query("SELECT p FROM Promotion p WHERE p.manager.Organization = :organization AND p.promotionstatus = :status")
    List<Promotion> findUnapprovedPromotionsByOrganization(@Param("organization")  String organization, @Param("status") promotionStatus status);

    @Query("SELECT SUM(ph.product.price) FROM PurchaseHistory ph WHERE ph.product.manager.id = :managerId AND ph.purchaseDate <= (SELECT MIN(p.startDate) FROM Promotion p WHERE p.manager.id = :managerId)")
    Double findTotalRevenueByManagerBeforePromotion(Long managerId);

//    @Query("SELECT SUM(ph.product.price) FROM PurchaseHistory ph JOIN ph.product p WHERE p.promotion.id = :promotionId AND ph.purchaseDate >= (SELECT startDate FROM Promotion WHERE id = :promotionId)")
//    Double findTotalRevenueByPromotion(Long promotionId);

    @Query("SELECT SUM(p.price) FROM Product p JOIN p.promotion pr JOIN pr.manager m WHERE m.id = :managerId GROUP BY p.category")
    Double findTotalRevenueByManagerAndCategory(Long managerId);
//    @Query("SELECT COUNT(*) FROM Promotion p JOIN p.InterestedCustomers ic WHERE p.id = :promotionId")
//    int countInterestedCustomers(Long promotionId);
    @Query("SELECT COUNT(ic) FROM Promotion p JOIN p.InterestedCustomers ic WHERE p.manager.id = :managerId")
    int countInterestedCustomersByManager(Long managerId);


//    @Query("SELECT COUNT(*) FROM Promotion p JOIN p.BoughtCustomers bc WHERE p.id = :promotionId")
//    int countBoughtCustomers(Long promotionId);

    @Query("SELECT COUNT(bc) FROM Promotion p JOIN p.BoughtCustomers bc WHERE p.manager.id = :managerId")
    int countBoughtCustomersByManager(Long managerId);


}
