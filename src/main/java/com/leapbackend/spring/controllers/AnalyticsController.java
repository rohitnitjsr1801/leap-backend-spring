package com.leapbackend.spring.controllers;

import com.leapbackend.spring.models.Analytics;
import com.leapbackend.spring.security.jwt.JwtUtils;
import com.leapbackend.spring.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/create")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<String> createAnalytics(@RequestParam("manager_id") Long managerId, @RequestHeader(name="Authorization") String token) {
        ResponseEntity<Void> tokenValidationResponse = validateToken(token);
        if (tokenValidationResponse != null) {
            return new ResponseEntity<>(tokenValidationResponse.getStatusCode());
        }

        analyticsService.createAnalytics(managerId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Analytics created");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Analytics> getAnalyticsById(@PathVariable Long id, @RequestHeader(name="Authorization") String token) {
        ResponseEntity<Void> tokenValidationResponse = validateToken(token);
        if (tokenValidationResponse != null) {
            return new ResponseEntity<>(null, tokenValidationResponse.getStatusCode());
        }

        Analytics analytics = analyticsService.getAnalyticsById(id);
        if (analytics != null) {
            return ResponseEntity.ok(analytics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<List<Analytics>> getAllAnalytics(@RequestHeader(name="Authorization") String token) {
        ResponseEntity<Void> tokenValidationResponse = validateToken(token);
        if (tokenValidationResponse != null) {
            return new ResponseEntity<>(null, tokenValidationResponse.getStatusCode());
        }

        List<Analytics> analyticsList = analyticsService.getAllAnalytics();
        return ResponseEntity.ok(analyticsList);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Analytics> updateAnalytics(@PathVariable Long id, @RequestHeader(name="Authorization") String token) {
        ResponseEntity<Void> tokenValidationResponse = validateToken(token);
        if (tokenValidationResponse != null) {
            return new ResponseEntity<>(null, tokenValidationResponse.getStatusCode());
        }

        Analytics updated = analyticsService.updateAnalytics(id);
        return ResponseEntity.ok(updated);
    }


    private ResponseEntity<Void> validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return null; // Token is valid
    }



}
