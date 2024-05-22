package com.leapbackend.spring.service;

import com.leapbackend.spring.models.Analytics;
import java.util.List;

public interface AnalyticsService {
    Analytics createAnalytics(Long managerId);
    Analytics getAnalyticsById(Long id);
    List<Analytics> getAllAnalytics();
}
