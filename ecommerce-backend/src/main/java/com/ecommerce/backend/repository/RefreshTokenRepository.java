package com.ecommerce.backend.repository;

import com.ecommerce.backend.entity.RefreshToken;
import com.ecommerce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndDeletedAtIsNull(String token);

    List<RefreshToken> findByUserAndDeletedAtIsNull(User user);

    List<RefreshToken> findByUserAndIsRevokedFalseAndDeletedAtIsNull(User user);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.user = :user AND rt.isRevoked = false")
    void revokeAllByUser(@Param("user") User user);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.token = :token")
    void revokeByToken(@Param("token") String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.deletedAt = CURRENT_TIMESTAMP WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.deletedAt = CURRENT_TIMESTAMP WHERE rt.isRevoked = true AND rt.updatedAt < :cutoff")
    void deleteRevokedTokensOlderThan(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user AND rt.isRevoked = false AND rt.deletedAt IS NULL")
    long countActiveTokensByUser(@Param("user") User user);

    boolean existsByTokenAndIsRevokedFalseAndDeletedAtIsNull(String token);
}