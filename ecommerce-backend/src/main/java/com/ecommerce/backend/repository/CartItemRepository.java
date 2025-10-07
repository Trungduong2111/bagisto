package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.ProductVariant;
import com.ecommerce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    List<CartItem> findByUserAndDeletedAtIsNull(User user);

    Optional<CartItem> findByUserAndProductAndProductVariantIsNull(User user, Product product);

    Optional<CartItem> findByUserAndProductAndProductVariant(User user, Product product, ProductVariant productVariant);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user = :user AND ci.product = :product AND " +
           "(:variant IS NULL AND ci.productVariant IS NULL OR ci.productVariant = :variant) AND ci.deletedAt IS NULL")
    Optional<CartItem> findByUserAndProductAndVariant(@Param("user") User user, 
                                                      @Param("product") Product product, 
                                                      @Param("variant") ProductVariant variant);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.user = :user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.deletedAt = CURRENT_TIMESTAMP WHERE ci.user = :user")
    void softDeleteByUser(@Param("user") User user);

    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.user = :user AND ci.deletedAt IS NULL")
    long countByUser(@Param("user") User user);

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user = :user AND ci.deletedAt IS NULL")
    Integer getTotalQuantityByUser(@Param("user") User user);

    boolean existsByUserAndProductAndProductVariantIsNull(User user, Product product);

    boolean existsByUserAndProductAndProductVariant(User user, Product product, ProductVariant productVariant);
}