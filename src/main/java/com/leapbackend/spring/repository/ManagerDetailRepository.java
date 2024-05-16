package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.ManagerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail,Long> {
    Optional<ManagerDetail> findByUserId(Long userId);
}
