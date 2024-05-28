package com.leapbackend.spring.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailResponse {
    private String name;
    private String description;
    private double price; // discounted price
    private String category;


}
