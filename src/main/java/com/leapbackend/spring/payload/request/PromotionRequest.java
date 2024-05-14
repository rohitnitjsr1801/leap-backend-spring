package com.leapbackend.spring.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PromotionRequest {
    private String gender;
    private String ageRange;
    private List<Long> productIds;
    private String purchaseFrequency;
    private String promotionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountRate;
}

