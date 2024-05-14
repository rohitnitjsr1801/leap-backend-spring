package com.leapbackend.spring.service.impl;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    public Product createProduct(Product product, User manager) {
        List<Product> productList=manager.getProductList();
        productList.add(product);
        manager.setProductList(productList);
//        userRepository.save(manager);
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}