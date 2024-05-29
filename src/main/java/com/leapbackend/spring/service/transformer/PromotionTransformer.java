package com.leapbackend.spring.service.transformer;

import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.PromotionResponse;

import java.util.List;

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
                .id(promotion.getId())
                .products(promotion.getProducts())
                .promotionType(promotion.getPromotionType())
                .discountRate(promotion.getDiscountRate())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .build();
    }

    public static void updatePromotionFromRequest(Promotion promotion, PromotionRequest promotionRequest, ManagerDetail managerDetail, List<Product> productList) {
        promotion.setManager(managerDetail);
        promotion.setProducts(productList);
        promotion.setPromotionType(promotionRequest.getPromotionType());
        promotion.setGender(promotionRequest.getGender());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setDiscountRate(promotionRequest.getDiscountRate());
        promotion.setAgeRange(promotionRequest.getAgeRange());
        promotion.setPurchaseFrequency(promotionRequest.getPurchaseFrequency());
    }
}
