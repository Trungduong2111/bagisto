package com.example.ecomm.service;

import com.example.ecomm.dto.ProductDto;
import com.example.ecomm.entity.Category;
import com.example.ecomm.entity.Product;
import com.example.ecomm.repository.CategoryRepository;
import com.example.ecomm.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Transactional
    public Product create(ProductDto dto) {
        Product p = new Product();
        apply(p, dto);
        return productRepository.save(p);
    }

    @Transactional
    public Product update(Long id, ProductDto dto) {
        Product p = getById(id);
        apply(p, dto);
        return productRepository.save(p);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    private void apply(Product p, ProductDto dto) {
        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setSku(dto.getSku());
        p.setStock(dto.getStock());
        if (dto.getCategoryId() != null) {
            Category c = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
            p.setCategory(c);
        } else {
            p.setCategory(null);
        }
    }
}
