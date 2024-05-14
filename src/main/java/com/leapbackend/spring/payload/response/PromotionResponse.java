package com.leapbackend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    private Long id;
    private String gender;
    private String ageRange;
    private List<Long> productIds;
    private String purchaseFrequency;
    private String promotionType;
    private double discountRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int interestedCount;
    private int buyCount;
}
