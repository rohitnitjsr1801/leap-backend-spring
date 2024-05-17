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
}
