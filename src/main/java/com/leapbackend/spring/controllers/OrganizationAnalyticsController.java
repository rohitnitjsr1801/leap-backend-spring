package com.leapbackend.spring.controllers;

import com.leapbackend.spring.models.OrganizationAnalytics;
import com.leapbackend.spring.security.jwt.JwtUtils;
import com.leapbackend.spring.service.OrganizationAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization-analytics")
public class OrganizationAnalyticsController {

    @Autowired
    private OrganizationAnalyticsService organizationAnalyticsService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> createOrganizationAnalytics(@RequestHeader(name="Authorization") String token) {
        ResponseEntity<Void> tokenValidationResponse = validateToken(token);
        if (tokenValidationResponse != null) {
            return new ResponseEntity<>(tokenValidationResponse.getStatusCode());
        }

        organizationAnalyticsService.createOrganizationAnalytics();
        return ResponseEntity.status(HttpStatus.CREATED).body("Organization Analytics created");
    }



    private ResponseEntity<Void> validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return null;
    }
}
