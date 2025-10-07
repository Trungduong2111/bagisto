package com.ecommerce.backend.security;

import com.ecommerce.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwt.access-token.secret:myAccessTokenSecret}")
    private String accessTokenSecret;

    @Value("${app.jwt.access-token.expiration:900000}") // 15 minutes
    private long accessTokenExpirationInMs;

    @Value("${app.jwt.refresh-token.secret:myRefreshTokenSecret}")
    private String refreshTokenSecret;

    @Value("${app.jwt.refresh-token.expiration:604800000}") // 7 days
    private long refreshTokenExpirationInMs;

    private final SecureRandom secureRandom = new SecureRandom();

    private SecretKey getAccessTokenSigningKey() {
        return Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
    }

    private SecretKey getRefreshTokenSigningKey() {
        return Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    // Access Token Methods
    public String generateAccessToken(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + accessTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("tokenType", "ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getAccessTokenSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateAccessToken(user);
    }

    // Refresh Token Methods
    public String generateRefreshToken(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + refreshTokenExpirationInMs);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("tokenType", "REFRESH")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getRefreshTokenSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Generate secure random refresh token (alternative approach)
    public String generateSecureRefreshToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    // Token Validation Methods
    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessTokenSigningKey(), "ACCESS");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshTokenSigningKey(), "REFRESH");
    }

    private boolean validateToken(String token, SecretKey signingKey, String expectedTokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("tokenType", String.class);
            return expectedTokenType.equals(tokenType);
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature for {} token", expectedTokenType);
        } catch (MalformedJwtException ex) {
            log.error("Invalid {} JWT token", expectedTokenType);
        } catch (ExpiredJwtException ex) {
            log.error("Expired {} JWT token", expectedTokenType);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported {} JWT token", expectedTokenType);
        } catch (IllegalArgumentException ex) {
            log.error("{} JWT claims string is empty", expectedTokenType);
        }
        return false;
    }

    // Token Information Extraction Methods
    public String getUserEmailFromAccessToken(String token) {
        return getUserEmailFromToken(token, getAccessTokenSigningKey());
    }

    public String getUserEmailFromRefreshToken(String token) {
        return getUserEmailFromToken(token, getRefreshTokenSigningKey());
    }

    private String getUserEmailFromToken(String token, SecretKey signingKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getUserIdFromAccessToken(String token) {
        return getUserIdFromToken(token, getAccessTokenSigningKey());
    }

    public Long getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token, getRefreshTokenSigningKey());
    }

    private Long getUserIdFromToken(String token, SecretKey signingKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    public String getRoleFromAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getAccessTokenSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public Date getExpirationDateFromAccessToken(String token) {
        return getExpirationDateFromToken(token, getAccessTokenSigningKey());
    }

    public Date getExpirationDateFromRefreshToken(String token) {
        return getExpirationDateFromToken(token, getRefreshTokenSigningKey());
    }

    private Date getExpirationDateFromToken(String token, SecretKey signingKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean isAccessTokenExpired(String token) {
        Date expiration = getExpirationDateFromAccessToken(token);
        return expiration.before(new Date());
    }

    public boolean isRefreshTokenExpired(String token) {
        Date expiration = getExpirationDateFromRefreshToken(token);
        return expiration.before(new Date());
    }

    // Backward compatibility methods
    @Deprecated
    public String generateToken(User user) {
        return generateAccessToken(user);
    }

    @Deprecated
    public String generateToken(Authentication authentication) {
        return generateAccessToken(authentication);
    }

    @Deprecated
    public String getUserEmailFromToken(String token) {
        return getUserEmailFromAccessToken(token);
    }

    @Deprecated
    public Long getUserIdFromToken(String token) {
        return getUserIdFromAccessToken(token);
    }

    @Deprecated
    public String getRoleFromToken(String token) {
        return getRoleFromAccessToken(token);
    }

    @Deprecated
    public boolean validateToken(String token) {
        return validateAccessToken(token);
    }

    @Deprecated
    public Date getExpirationDateFromToken(String token) {
        return getExpirationDateFromAccessToken(token);
    }

    @Deprecated
    public boolean isTokenExpired(String token) {
        return isAccessTokenExpired(token);
    }

    // Utility methods
    public long getAccessTokenExpirationInMs() {
        return accessTokenExpirationInMs;
    }

    public long getRefreshTokenExpirationInMs() {
        return refreshTokenExpirationInMs;
    }
}