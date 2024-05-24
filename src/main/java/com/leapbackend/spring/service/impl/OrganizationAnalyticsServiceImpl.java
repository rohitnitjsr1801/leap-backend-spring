package com.leapbackend.spring.service.impl;

import com.leapbackend.spring.models.Analytics;
import com.leapbackend.spring.models.OrganizationAnalytics;
import com.leapbackend.spring.repository.AnalyticsRepository;
import com.leapbackend.spring.repository.OrganizationAnalyticsRepository;
import com.leapbackend.spring.service.OrganizationAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganizationAnalyticsServiceImpl implements OrganizationAnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private OrganizationAnalyticsRepository organizationAnalyticsRepository;

    @Override
    public OrganizationAnalytics createOrganizationAnalytics() {
        List<Analytics> allAnalytics = analyticsRepository.findAll();

        Map<String, Double> organizationRevenueMap = allAnalytics.stream()
                .collect(Collectors.groupingBy(
                        analytics -> analytics.getManager().getOrganization(),
                        Collectors.summingDouble(analytics -> analytics.getPreRevenue() + analytics.getPostRevenue())
                ));

        organizationRevenueMap.forEach((organization, totalRevenue) -> {
            OrganizationAnalytics organizationAnalytics = new OrganizationAnalytics();
            organizationAnalytics.setOrganizationName(organization);
            organizationAnalytics.setTotalRevenue(totalRevenue);
            organizationAnalytics.setLastUpdated(LocalDateTime.now());

            organizationAnalyticsRepository.save(organizationAnalytics);
        });

        return null; // This method can be improved to return meaningful data if required
    }

}
