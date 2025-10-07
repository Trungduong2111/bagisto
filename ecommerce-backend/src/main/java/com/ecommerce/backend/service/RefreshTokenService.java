package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.RefreshToken;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.RefreshTokenRepository;
import com.ecommerce.backend.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.jwt.refresh-token.max-tokens-per-user:5}")
    private int maxTokensPerUser;

    public RefreshToken createRefreshToken(User user, HttpServletRequest request) {
        // Clean up old tokens if user has too many
        cleanupOldTokensForUser(user);

        // Generate refresh token
        String tokenValue = tokenProvider.generateSecureRefreshToken();
        
        RefreshToken refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpirationInMs() / 1000))
                .deviceInfo(extractDeviceInfo(request))
                .ipAddress(extractIpAddress(request))
                .isRevoked(false)
                .build();

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token for user: {}", user.getEmail());
        return savedToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenAndDeletedAtIsNull(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public boolean isValidRefreshToken(String token) {
        return refreshTokenRepository.findByTokenAndDeletedAtIsNull(token)
                .map(RefreshToken::isValid)
                .orElse(false);
    }

    public void revokeToken(String token) {
        refreshTokenRepository.revokeByToken(token);
        log.info("Revoked refresh token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
    }

    public void revokeAllTokensForUser(User user) {
        refreshTokenRepository.revokeAllByUser(user);
        log.info("Revoked all refresh tokens for user: {}", user.getEmail());
    }

    public void deleteToken(RefreshToken token) {
        token.softDelete();
        refreshTokenRepository.save(token);
        log.info("Deleted refresh token for user: {}", token.getUser().getEmail());
    }

    @Transactional(readOnly = true)
    public List<RefreshToken> getActiveTokensForUser(User user) {
        return refreshTokenRepository.findByUserAndIsRevokedFalseAndDeletedAtIsNull(user);
    }

    @Transactional(readOnly = true)
    public long getActiveTokenCountForUser(User user) {
        return refreshTokenRepository.countActiveTokensByUser(user);
    }

    private void cleanupOldTokensForUser(User user) {
        long activeTokenCount = getActiveTokenCountForUser(user);
        
        if (activeTokenCount >= maxTokensPerUser) {
            List<RefreshToken> activeTokens = getActiveTokensForUser(user);
            
            // Sort by creation date and revoke oldest tokens
            activeTokens.stream()
                    .sorted((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                    .limit(activeTokenCount - maxTokensPerUser + 1)
                    .forEach(token -> {
                        token.setIsRevoked(true);
                        refreshTokenRepository.save(token);
                    });
            
            log.info("Cleaned up {} old refresh tokens for user: {}", 
                    activeTokenCount - maxTokensPerUser + 1, user.getEmail());
        }
    }

    private String extractDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            // Extract basic device info from User-Agent
            if (userAgent.contains("Mobile")) {
                return "Mobile Device";
            } else if (userAgent.contains("Tablet")) {
                return "Tablet";
            } else {
                return "Desktop";
            }
        }
        return "Unknown Device";
    }

    private String extractIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    // Scheduled cleanup tasks
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.debug("Cleaned up expired refresh tokens");
    }

    @Scheduled(fixedRate = 86400000) // Run every day
    public void cleanupRevokedTokens() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7); // Keep revoked tokens for 7 days
        refreshTokenRepository.deleteRevokedTokensOlderThan(cutoff);
        log.debug("Cleaned up old revoked refresh tokens");
    }
}