package com.leapbackend.spring.service.impl;

import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.PromotionResponse;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.PromotionRepository;
import com.leapbackend.spring.service.PromotionService;
import com.leapbackend.spring.service.transformer.PromotionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ManagerDetailRepository managerDetailRepository;

    @Autowired
    ProductRepository productRepository;

    public PromotionResponse createPromotion(PromotionRequest promotionRequest) {
        ManagerDetail managerDetail = managerDetailRepository.findByUserId(promotionRequest.getManagerId()).get();
        Promotion promotion = PromotionTransformer.promotionRequestToPromotion(promotionRequest, managerDetail);

        List<Product> productList = new ArrayList<>();
        for(Long productId: promotionRequest.getProductIds()) {
            Product product = productRepository.findById(productId).get();
            product.setPromotion(promotion);
            productList.add(product);
        }
        promotion.setProducts(productList);
        promotionRepository.save(promotion);

        return PromotionTransformer.promotionToPromotionResponse(promotion);
    }
}
