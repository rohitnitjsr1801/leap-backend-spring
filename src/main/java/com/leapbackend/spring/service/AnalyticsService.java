package com.leapbackend.spring.service;

import com.leapbackend.spring.models.Analytics;
import java.util.List;
import java.util.Optional;

public interface AnalyticsService {
    Analytics createAnalytics(Long managerId);
    Analytics getAnalyticsById(Long id);
    List<Analytics> getAllAnalytics();

    Analytics updateAnalytics(Long id);

    Analytics findAnalyticsByManagerId(Long managerId);
}
