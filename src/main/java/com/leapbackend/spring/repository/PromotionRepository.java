package com.leapbackend.spring.repository;

import com.leapbackend.spring.enums.promotionStatus;
import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByManager(ManagerDetail ownerDetail);

    List<Promotion> findByManagerAndPromotionstatus(ManagerDetail ownerDetail, promotionStatus promotionStatus);
}
