package com.leapbackend.spring.service;

import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.ProductDetailResponse;
import com.leapbackend.spring.payload.response.PromotionResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionRequest request);

    PromotionResponse updatePromotion(Long id, PromotionRequest request);

    Promotion getPromotion(Long id);

    List<Promotion> getPromotionList(Long id);

    ResponseEntity<String> approvePromotion(Long promotionId);

    ResponseEntity<String> buyPromotion(Long promotionId, Long customerId);

    ResponseEntity<String> interestedForPromotion(Long promotionId, Long customerId);

    PromotionResponse getPromotionByProductId(Long productId);

    ResponseEntity<String> deletePromotion(Long promotionId);


    List<ProductDetailResponse> getInterestedPromotions(Long customerId);

    List<ProductDetailResponse> getBoughtPromotions(Long customerId);
}
