package com.leapbackend.spring.service;

import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.PromotionResponse;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionRequest request);

    PromotionResponse updatePromotion(Long id, PromotionRequest request);

    PromotionResponse getPromotion(Long id);
}
