package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.enums.ProductStatus;
import com.ecommerce.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("SKU already exists");
        }
        
        if (productRepository.existsBySlug(product.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        Product savedProduct = productRepository.save(product);
        log.info("Created new product with SKU: {}", product.getSku());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        
        // Check SKU uniqueness if changed
        if (!product.getSku().equals(productDetails.getSku()) && 
            productRepository.existsBySku(productDetails.getSku())) {
            throw new RuntimeException("SKU already exists");
        }
        
        // Check slug uniqueness if changed
        if (!product.getSlug().equals(productDetails.getSlug()) && 
            productRepository.existsBySlug(productDetails.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setShortDescription(productDetails.getShortDescription());
        product.setSku(productDetails.getSku());
        product.setPrice(productDetails.getPrice());
        product.setComparePrice(productDetails.getComparePrice());
        product.setCostPrice(productDetails.getCostPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setMinStockLevel(productDetails.getMinStockLevel());
        product.setWeight(productDetails.getWeight());
        product.setDimensions(productDetails.getDimensions());
        product.setStatus(productDetails.getStatus());
        product.setIsFeatured(productDetails.getIsFeatured());
        product.setIsDigital(productDetails.getIsDigital());
        product.setRequiresShipping(productDetails.getRequiresShipping());
        product.setMetaTitle(productDetails.getMetaTitle());
        product.setMetaDescription(productDetails.getMetaDescription());
        product.setSlug(productDetails.getSlug());
        product.setCategory(productDetails.getCategory());
        
        Product updatedProduct = productRepository.save(product);
        log.info("Updated product with id: {}", id);
        return updatedProduct;
    }

    public Product updateProductStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        product.setStockQuantity(quantity);
        
        // Update status based on stock
        if (quantity <= 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.ACTIVE);
        }
        
        Product updatedProduct = productRepository.save(product);
        log.info("Updated stock for product with id: {} to {}", id, quantity);
        return updatedProduct;
    }

    public Product updateProductStatus(Long id, ProductStatus status) {
        Product product = getProductById(id);
        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);
        log.info("Updated status for product with id: {} to {}", id, status);
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.softDelete();
        productRepository.save(product);
        log.info("Soft deleted product with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .filter(product -> !product.isDeleted())
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlugAndDeletedAtIsNull(slug);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByDeletedAtIsNull(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productRepository.findByStatusAndDeletedAtIsNull(ProductStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategoryAndStatusAndDeletedAtIsNull(category, ProductStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> getFeaturedProducts() {
        return productRepository.findFeaturedProducts();
    }

    @Transactional(readOnly = true)
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.findFeaturedProducts(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String search, Pageable pageable) {
        return productRepository.findBySearchTerm(search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchActiveProducts(String search, Pageable pageable) {
        return productRepository.findActiveBySearchTerm(search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategoryAndPriceRange(Category category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByCategoryAndPriceRange(category, minPrice, maxPrice, pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }

    @Transactional(readOnly = true)
    public long getTotalProductsCount() {
        return productRepository.countActiveProducts();
    }

    @Transactional(readOnly = true)
    public long getProductsCountByStatus(ProductStatus status) {
        return productRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long getProductsCountByCategory(Category category) {
        return productRepository.countByCategory(category);
    }
}