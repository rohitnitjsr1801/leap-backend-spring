package com.leapbackend.spring.repository;

import com.leapbackend.spring.models.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

    Optional<Analytics> findByManager_Id(Long manager_id);
}
