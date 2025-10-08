package com.ecommerce.working.repository;

import com.ecommerce.working.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();
    boolean existsBySku(String sku);
}