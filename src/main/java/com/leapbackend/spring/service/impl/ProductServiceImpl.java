package com.leapbackend.spring.service.impl;

import com.leapbackend.spring.models.ManagerDetail;
import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ManagerDetailRepository managerDetailRepository;



    public Product createProduct(Product product, User manager) {
        Optional<ManagerDetail> response = managerDetailRepository.findByUserId(manager.getId());
        System.out.println("Entered");
        ManagerDetail managerDetail = response.get();
        List<Product> productList=productRepository.findAllByManagerId(managerDetail.getId());
        productList.add(product);
        managerDetail.setProducts(productList);
        managerDetailRepository.save(managerDetail);
        product.setManager(managerDetail);
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}