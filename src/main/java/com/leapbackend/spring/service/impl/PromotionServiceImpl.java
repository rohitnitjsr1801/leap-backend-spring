package com.leapbackend.spring.service.impl;

import com.leapbackend.spring.enums.promotionStatus;
import com.leapbackend.spring.models.CustomerDetail;
import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.ProductDetailResponse;
import com.leapbackend.spring.payload.response.PromotionResponse;
import com.leapbackend.spring.repository.CustomerDetailRepository;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.PromotionRepository;
import com.leapbackend.spring.service.PromotionService;
import com.leapbackend.spring.service.transformer.PromotionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ManagerDetailRepository managerDetailRepository;

    @Autowired
    CustomerDetailRepository customerDetailRepository;

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
        promotion.setPromotionstatus(promotionStatus.NOT_APPROVED);
        Promotion SavedPromotion = promotionRepository.save(promotion);

        return PromotionTransformer.promotionToPromotionResponse(SavedPromotion);
    }

    public ResponseEntity<String> deletePromotion(Long promotionId) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(promotionId);
        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            for(Product product: promotion.getProducts()) {
                product.setPromotion(null);
                productRepository.save(product);
            }
        } else {
            return null;
        }
        promotionRepository.deleteById(promotionId);
        return ResponseEntity.ok("Promotion deleted successfully!");
    }

    public ResponseEntity<String> buyPromotion(Long promotionId, Long customerId) {
        Promotion promotion = promotionRepository.findById(promotionId).get();
        CustomerDetail customerDetail = customerDetailRepository.findByUserId(customerId).get();

        promotion.setBuyCount(promotion.getBuyCount() + 1);
        promotion.getBoughtCustomers().add(customerDetail);
        customerDetail.getBoughtPromotions().add(promotion);
        customerDetailRepository.save(customerDetail);
        return ResponseEntity.ok("Promotion bought successfully!");
    }

    public ResponseEntity<String> interestedForPromotion(Long promotionId, Long customerId) {
        Promotion promotion = promotionRepository.findById(promotionId).get();
        CustomerDetail customerDetail = customerDetailRepository.findByUserId(customerId).get();

        promotion.setInterestedCount(promotion.getInterestedCount() + 1);
        promotion.getInterestedCustomers().add(customerDetail);
        customerDetail.getInterestedPromotions().add(promotion);
        customerDetailRepository.save(customerDetail);

        return ResponseEntity.ok("Promotion marked as interested, successfully!");
    }

    public PromotionResponse getPromotionByProductId(Long productId) {
        Product product = productRepository.findById(productId).get();
        if(product.getPromotion() != null && product.getPromotion().getPromotionstatus() == promotionStatus.APPROVED)
            return PromotionTransformer.promotionToPromotionResponse(product.getPromotion());
        return null;
    }

    public List<Promotion> getPromotionList(Long id)
    {
        Optional<ManagerDetail> managerDetail=managerDetailRepository.findByUserId(id);
        if(managerDetail.isEmpty())
        {
            return new ArrayList<>();
        }
        ManagerDetail ownerDetail=managerDetail.get();
        return promotionRepository.findUnapprovedPromotionsByOrganization(ownerDetail.getOrganization(), promotionStatus.NOT_APPROVED);
    }

    public ResponseEntity<String> approvePromotion(Long promotionId)
    {
        Optional<Promotion> response = promotionRepository.findById(promotionId);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        else {
            Promotion promotion=response.get();
            promotion.setPromotionstatus(promotionStatus.APPROVED);
            promotionRepository.save(promotion);
            return ResponseEntity.ok("Promotion approved successfully");
        }
    }

    public PromotionResponse updatePromotion(Long id, PromotionRequest promotionRequest) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            ManagerDetail managerDetail = managerDetailRepository.findByUserId(promotionRequest.getManagerId()).orElse(null);
            if (managerDetail == null) {
                return null;
            }
            for(Product product: promotion.getProducts()) {
                product.setPromotion(null);
                productRepository.save(product);
            }
            List<Product> productList = new ArrayList<>();
            for(Long productId: promotionRequest.getProductIds()) {
                Optional<Product> optionalProduct = productRepository.findById(productId);
                if(optionalProduct.isEmpty())
                    return null;
                Product product = optionalProduct.get();
                product.setPromotion(promotion);
                productList.add(product);
            }
            PromotionTransformer.updatePromotionFromRequest(promotion, promotionRequest, managerDetail, productList);
            Promotion updatedPromotion = promotionRepository.save(promotion);
            return PromotionTransformer.promotionToPromotionResponse(updatedPromotion);
        } else {
            return null;
        }
    }

    public Promotion getPromotion(Long id) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(id);
        return promotionOptional.get();
    }

    @Override
    public List<ProductDetailResponse> getInterestedPromotions(Long customerId) {
        List<Promotion> promotions = customerDetailRepository.findInterestedPromotionsByCustomerId(customerId);
        return promotions.stream()
                .flatMap(promotion -> promotion.getProducts().stream()
                        .map(product -> mapToProductDetailResponse(product, promotion.getDiscountRate())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDetailResponse> getBoughtPromotions(Long customerId) {
        List<Promotion> promotions = customerDetailRepository.findBoughtPromotionsByCustomerId(customerId);
        return promotions.stream()
                .flatMap(promotion -> promotion.getProducts().stream()
                        .map(product -> mapToProductDetailResponse(product, promotion.getDiscountRate())))
                .collect(Collectors.toList());
    }

    private ProductDetailResponse mapToProductDetailResponse(Product product, double discountRate) {
        ProductDetailResponse response = new ProductDetailResponse();
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory().name());
        double discountedPrice = product.getPrice() - (product.getPrice() * discountRate / 100);
        response.setPrice(discountedPrice);
        return response;
    }
}
