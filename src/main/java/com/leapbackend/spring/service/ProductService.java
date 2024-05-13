package com.leapbackend.spring.service;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;


import java.util.List;

public interface ProductService {
    Product createProduct(Product product, User manager);
    List<Product> getAllProducts();
    // Other service methods for updating, deleting, and getting a single product
}
