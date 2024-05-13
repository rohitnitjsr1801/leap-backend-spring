package com.leapbackend.spring.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    private Long id;
    private String gender;
    private String ageRange;
    private Long productId;
    private String purchaseFrequency;
    private String promotionType;
    private double discountRate;
    private String startDate;
    private String endDate;
}
