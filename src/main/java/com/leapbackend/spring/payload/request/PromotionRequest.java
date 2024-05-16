package com.leapbackend.spring.payload.request;

import com.leapbackend.spring.enums.AgeRange;
import com.leapbackend.spring.enums.Gender;
import com.leapbackend.spring.enums.PurchaseFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PromotionRequest {
    private Gender gender;
    private AgeRange ageRange;
    private List<Long> productIds;
    private PurchaseFrequency purchaseFrequency;
    private String promotionType;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountRate;
    private Long managerId;
}

