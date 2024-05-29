package com.leapbackend.spring.controllers;

import com.leapbackend.spring.models.*;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.ProductRepository;
import com.leapbackend.spring.repository.PurchaseHistoryRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.security.jwt.JwtUtils;
import com.leapbackend.spring.service.ProductService;
import com.leapbackend.spring.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PurchaseHistoryRepository purchaseHistoryRepository;

    @Autowired
    ManagerDetailRepository managerDetailRepository;

    @PostMapping("/product")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product,
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
//        Product createdProduct = productService.createProduct(product);
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

    @GetMapping("/getProductByProductId")
    public ResponseEntity<Product> getProductByProductId(@RequestParam Long id,@RequestHeader(name="Authorization") String token) {
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
        Optional<Product> productOptional=productRepository.findById(id);
        if(productOptional.isEmpty())
        {
            return null;
        }
        return ResponseEntity.ok(productOptional.get());
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
        Optional<ManagerDetail> response = managerDetailRepository.findByUserId(managerId);
        ManagerDetail managerDetail = response.get();
        List<Product> products = productRepository.findAllByManagerId(managerDetail.getId());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getPromotionId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> getPromotionId(@RequestParam Long productId,
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
        Optional<Product> optionalProduct=productRepository.findById(productId);
        Product product=optionalProduct.get();;
        Promotion promotion=product.getPromotion();
        if(promotion==null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion not found.");
        }
        return ResponseEntity.ok(""+promotion.getId());
    }


    @PostMapping("/buyProduct/{productId}")
    public ResponseEntity<String> buyProduct(@PathVariable Long productId, @RequestHeader(name = "Authorization") String token) {
        // Extract username from JWT token
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));

        User customer = userRepository.findByUsername(username).orElse(null);

        // Validate if the user exists and is a customer
        if (customer == null || !customer.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_CUSTOMER))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only customers can buy products.");
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        Product product = optionalProduct.get();

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setCustomer(customer);
        purchaseHistory.setProduct(product);
        purchaseHistory.setPurchaseDate(LocalDate.now());

        // Save the purchase history
        purchaseHistoryRepository.save(purchaseHistory);

        return ResponseEntity.ok("Product purchased successfully!");
    }

    @GetMapping("/myPurchasedProducts")
    public ResponseEntity<List<Product>> getMyPurchasedProducts(@RequestHeader(name = "Authorization") String token) {
        // Extracting username from JWT token
        String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));

        User customer = userRepository.findByUsername(username).orElse(null);

        // Checking if the user exists or not and is a customer
        if (customer == null || !customer.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_CUSTOMER))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<PurchaseHistory> purchaseHistoryList = purchaseHistoryRepository.findByCustomer(customer);

        List<Product> purchasedProducts = purchaseHistoryList.stream()
                .map(PurchaseHistory::getProduct)
                .collect(Collectors.toList());

        return ResponseEntity.ok(purchasedProducts);
    }

    @PutMapping("/product/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @Valid @RequestBody Product product,
                                                @RequestHeader(name = "Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }

        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        User manager = userOptional.get();
        Product updatedProduct = productService.updateProduct(id, product, manager);
        if (updatedProduct != null) {
            return ResponseEntity.ok("Product updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('OWNER')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @RequestHeader(name = "Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<Product>> getProductsForOwner(@RequestParam ("owner_id") Long ownerId,
                                                             @RequestHeader(name = "Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String jwtToken = token.substring(7);

        if (!jwtUtils.validateJwtToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(productService.getProductsByOrganization(ownerId));
    }

}

