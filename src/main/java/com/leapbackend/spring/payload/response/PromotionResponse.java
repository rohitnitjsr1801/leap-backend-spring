package com.leapbackend.spring.payload.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResponse {
    private Long id;
    private String promotionType;
    private double discountRate;
    private LocalDate startDate;
    private LocalDate endDate;
}
