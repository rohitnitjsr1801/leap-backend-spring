package com.leapbackend.spring.controllers;

import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.PromotionResponse;
import com.leapbackend.spring.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.leapbackend.spring.security.jwt.JwtUtils;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<PromotionResponse> createPromotion(@Valid @RequestBody PromotionRequest request,
                                                             @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        PromotionResponse response = promotionService.createPromotion(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
