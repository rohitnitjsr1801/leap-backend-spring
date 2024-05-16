package com.leapbackend.spring.service.transformer;

import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.PromotionResponse;

public class PromotionTransformer {

    public static Promotion promotionRequestToPromotion(PromotionRequest promotionRequest, ManagerDetail managerDetail) {
        return Promotion.builder()
                .manager(managerDetail)
                .promotionType(promotionRequest.getPromotionType())
                .gender(promotionRequest.getGender())
                .startDate(promotionRequest.getStartDate())
                .endDate(promotionRequest.getEndDate())
                .discountRate(promotionRequest.getDiscountRate())
                .ageRange(promotionRequest.getAgeRange())
                .purchaseFrequency(promotionRequest.getPurchaseFrequency())
                .build();
    }

    public static PromotionResponse promotionToPromotionResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .promotionType(promotion.getPromotionType())
                .discountRate(promotion.getDiscountRate())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .build();
    }
}
