package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Product> findBySku(String sku);

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryAndDeletedAtIsNull(Category category);

    Page<Product> findByDeletedAtIsNull(Pageable pageable);

    Page<Product> findByStatusAndDeletedAtIsNull(ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryAndStatusAndDeletedAtIsNull(Category category, ProductStatus status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE' AND p.isFeatured = true")
    List<Product> findFeaturedProducts();

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE' AND p.isFeatured = true")
    Page<Product> findFeaturedProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE' AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> findActiveBySearchTerm(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE' AND " +
           "p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice, 
                                   Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.status = 'ACTIVE' AND " +
           "p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByCategoryAndPriceRange(@Param("category") Category category,
                                              @Param("minPrice") BigDecimal minPrice,
                                              @Param("maxPrice") BigDecimal maxPrice,
                                              Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.stockQuantity <= p.minStockLevel")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.stockQuantity = 0")
    List<Product> findOutOfStockProducts();

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deletedAt IS NULL")
    long countActiveProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deletedAt IS NULL AND p.status = :status")
    long countByStatus(@Param("status") ProductStatus status);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deletedAt IS NULL AND p.category = :category")
    long countByCategory(@Param("category") Category category);
}