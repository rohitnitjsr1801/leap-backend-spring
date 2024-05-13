package com.leapbackend.spring.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PromotionRequest {
    private String gender;
    private String ageRange;
    private Long productId;
    private String purchaseFrequency;
    private String promotionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountRate;
}
