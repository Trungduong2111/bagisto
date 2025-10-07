package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.enums.OrderStatus;
import com.ecommerce.backend.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUser(User user);

    Page<Order> findByUser(User user, Pageable pageable);

    List<Order> findByStatus(OrderStatus status);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);

    Page<Order> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

    Page<Order> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND " +
           "(o.orderNumber LIKE CONCAT('%', :search, '%') OR " +
           "CONCAT(o.shippingFirstName, ' ', o.shippingLastName) LIKE CONCAT('%', :search, '%') OR " +
           "o.user.email LIKE CONCAT('%', :search, '%'))")
    Page<Order> findBySearchTerm(@Param("search") String search, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.deletedAt IS NULL AND " +
           "(o.orderNumber LIKE CONCAT('%', :search, '%') OR " +
           "o.status = :status)")
    Page<Order> findByUserAndSearchTerm(@Param("user") User user, 
                                        @Param("search") String search, 
                                        @Param("status") OrderStatus status, 
                                        Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.deletedAt IS NULL AND o.paymentStatus = 'PAID'")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.deletedAt IS NULL AND o.paymentStatus = 'PAID' AND " +
           "o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.deletedAt IS NULL")
    long countActiveOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.deletedAt IS NULL AND o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user AND o.deletedAt IS NULL")
    long countByUser(@Param("user") User user);

    @Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.status IN ('PENDING', 'CONFIRMED') AND " +
           "o.createdAt < :cutoffTime")
    List<Order> findPendingOrdersOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

    boolean existsByOrderNumber(String orderNumber);
}