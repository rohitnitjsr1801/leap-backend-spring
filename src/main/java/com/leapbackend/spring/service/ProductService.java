package com.leapbackend.spring.service;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;


import java.util.List;

public interface ProductService {
    Product createProduct(Product product, User manager);
    List<Product> getAllProducts();
    Product updateProduct(Long productId, Product product, User manager);
    void deleteProduct(Long id);
}
