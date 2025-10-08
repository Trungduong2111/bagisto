package com.ecommerce.working.controller;

import com.ecommerce.working.entity.Product;
import com.ecommerce.working.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null || !product.getIsActive()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            String sku = (String) request.get("sku");
            Double priceDouble = ((Number) request.get("price")).doubleValue();
            Integer stockQuantity = ((Number) request.get("stockQuantity")).intValue();
            
            if (productRepository.existsBySku(sku)) {
                response.put("success", false);
                response.put("message", "SKU already exists");
                return ResponseEntity.badRequest().body(response);
            }
            
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setSku(sku);
            product.setPrice(BigDecimal.valueOf(priceDouble));
            product.setStockQuantity(stockQuantity);
            product.setIsActive(true);
            
            Product savedProduct = productRepository.save(product);
            
            response.put("success", true);
            response.put("message", "Product created successfully");
            response.put("product", savedProduct);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to create product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}