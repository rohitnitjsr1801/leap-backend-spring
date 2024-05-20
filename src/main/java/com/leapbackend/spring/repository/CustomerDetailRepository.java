package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail,Long> {
    Optional<CustomerDetail> findByUserId(Long userId);
}
