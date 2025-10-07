package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    Optional<Category> findBySlugAndDeletedAtIsNull(String slug);

    List<Category> findByParentIsNullAndDeletedAtIsNullOrderBySortOrderAsc();

    List<Category> findByParentAndDeletedAtIsNullOrderBySortOrderAsc(Category parent);

    List<Category> findByIsActiveAndDeletedAtIsNullOrderBySortOrderAsc(Boolean isActive);

    Page<Category> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL AND " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Category> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.deletedAt IS NULL AND c.isActive = true ORDER BY c.sortOrder ASC")
    List<Category> findActiveRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parent = :parent AND c.deletedAt IS NULL AND c.isActive = true ORDER BY c.sortOrder ASC")
    List<Category> findActiveChildCategories(@Param("parent") Category parent);

    boolean existsBySlug(String slug);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.deletedAt IS NULL")
    long countActiveCategories();
}