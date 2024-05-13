package com.leapbackend.spring.controllers;

import com.leapbackend.spring.models.Product;
import com.leapbackend.spring.models.User;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.security.jwt.JwtUtils;
import com.leapbackend.spring.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductServiceImpl productService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/product")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product,
                                                 @RequestHeader(name="Authorization") String token) {
//        System.out.println(product);
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


        // Token is valid
        Product createdProduct = productRepository.save(product);
        return ResponseEntity.ok(createdProduct);
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product,
                                                 @RequestParam("managerId") Long managerId,
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

        Optional<User> user = userRepository.findById(managerId);
        if (user.isEmpty()) {
            // User with the given ID not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User manager=user.get();
        Product createdProduct=productService.createProduct(product,manager);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<Object> getAllProducts(@RequestHeader(name="Authorization") String token) {
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

        // Token is valid
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getProductById")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Object> getProducts(@RequestParam Long managerId,
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

        // Token is valid
        User manager = userRepository.findById(managerId).orElse(null);
        if (manager == null) {
            // Manager with the given ID not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Product> products = manager.getProductList();
        return ResponseEntity.ok(products);
    }
}
