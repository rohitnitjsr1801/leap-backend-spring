package com.leapbackend.spring.controllers;

import com.leapbackend.spring.enums.AgeRange;
import com.leapbackend.spring.enums.Gender;
import com.leapbackend.spring.enums.promotionStatus;
import com.leapbackend.spring.models.CustomerDetail;
import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Promotion;
import com.leapbackend.spring.models.User;
import com.leapbackend.spring.payload.request.PromotionRequest;
import com.leapbackend.spring.payload.response.ProductDetailResponse;
import com.leapbackend.spring.payload.response.PromotionResponse;
import com.leapbackend.spring.repository.CustomerDetailRepository;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.PromotionRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.service.PromotionService;
import com.leapbackend.spring.service.transformer.PromotionTransformer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.leapbackend.spring.security.jwt.JwtUtils;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerDetailRepository managerDetailRepository;

    @Autowired
    private CustomerDetailRepository customerDetailRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<PromotionResponse> createPromotion(@Valid @RequestBody PromotionRequest request,
                                                             @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        PromotionResponse response = promotionService.createPromotion(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<String> deletePromotion(@RequestParam ("promotion_id") Long promotionId,
                                                  @RequestHeader(name="Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return promotionService.deletePromotion(promotionId);
    }

    @PutMapping("/buy")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> buyPromotion(@RequestParam("promotion_id") Long promotionId,
                                               @RequestParam("customer_id") Long customerId,
                                               @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return promotionService.buyPromotion(promotionId, customerId);
    }


    @GetMapping("/checkIfPromotionApplicableForCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> checkIfPromotionApplicable(@RequestParam("promotion_id") Long promotionId,
                                               @RequestParam("customer_id") Long customerId,
                                               @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<CustomerDetail> customerDetailOptional=customerDetailRepository.findByUserId(customerId);
        CustomerDetail customerDetail=customerDetailOptional.get();
        int age1=customerDetail.getAge();
        Gender gender1=customerDetail.getGender();
        Optional<Promotion> promotionOptional=promotionRepository.findById(promotionId);
        Promotion promotion=promotionOptional.get();
        AgeRange age2=promotion.getAgeRange();
        Gender gender2=promotion.getGender();
        double discount=promotion.getDiscountRate();
        if(gender1.equals(gender2)&&promotion.getPromotionstatus().equals(promotionStatus.APPROVED))
        {
            if(age2.equals(AgeRange.ADULT))
            {
                if(age1>=18&&age1<=24)
                {
                    return ResponseEntity.ok(""+discount);
                }
            }
            else if(age2.equals(AgeRange.OLD))
            {
                if(age1>=40)
                {
                    return ResponseEntity.ok(""+discount);
                }
            }
            else if(age2.equals(AgeRange.TEEN))
            {
                if(age1>=10&&age1<18)
                {
                    return ResponseEntity.ok(""+discount);
                }
            }
            else if(age2.equals(AgeRange.CHILDREN))
            {
                if(age1>0&&age1<10)
                {
                    return ResponseEntity.ok(""+discount);
                }
            }
            else if(age2.equals(AgeRange.YOUNG_ADULT)) {
                if (age1 > 24 && age1 < 40) {
                    return ResponseEntity.ok("" + discount);
                }
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PutMapping("/interested")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> interestedPromotion(@RequestParam("promotion_id") Long promotionId,
                                                          @RequestParam("customer_id") Long customerId,
                                                          @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return promotionService.interestedForPromotion(promotionId, customerId);
    }

    @PutMapping("/approve/{promotionId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> updatePromotionStatus(@PathVariable Long promotionId,
                                                  @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return promotionService.approvePromotion(promotionId);
    }

    @GetMapping("ownerPromotions/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Object> getPromotionOfOwner(@PathVariable Long id,
                                                          @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Promotion> promotion=promotionService.getPromotionList(id);
        return ResponseEntity.ok(promotion);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<String> updatePromotion(@PathVariable Long id,
                                                  @Valid @RequestBody PromotionRequest request,
                                                  @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        PromotionResponse response = promotionService.updatePromotion(id, request);
        if (response != null) {
            return ResponseEntity.ok("Promotion updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Promotion> getPromotion(@PathVariable Long id,
                                                          @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Promotion response = promotionService.getPromotion(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<PromotionResponse> getPromotionByProduct(@RequestParam("product_id") Long productId,
                                                           @RequestHeader(name="Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            // Token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extracting JWT token from the Authorization header
        String jwtToken = token.substring(7);

        // Verifying the JWT token
        if (!jwtUtils.validateJwtToken(jwtToken)) {
            // Token is invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        PromotionResponse response = promotionService.getPromotionByProductId(productId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/interested/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductDetailResponse>> getInterestedPromotions(
            @PathVariable Long customerId,
            @RequestHeader(name = "Authorization") String token) {

        Optional<CustomerDetail> customerDetailOptional = customerDetailRepository.findByUserId(customerId);
        if (!customerDetailOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Long userId = customerDetailOptional.get().getId();

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<ProductDetailResponse> products = promotionService.getInterestedPromotions(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/bought/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductDetailResponse>> getBoughtPromotions(
            @PathVariable Long customerId,
            @RequestHeader(name = "Authorization") String token) {

        Optional<CustomerDetail> customerDetailOptional = customerDetailRepository.findByUserId(customerId);
        if (!customerDetailOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Long userId = customerDetailOptional.get().getId();

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<ProductDetailResponse> products = promotionService.getBoughtPromotions(userId);
        return ResponseEntity.ok(products);
    }


}
