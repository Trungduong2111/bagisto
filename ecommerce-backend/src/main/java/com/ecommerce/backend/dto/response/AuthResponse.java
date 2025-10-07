package com.ecommerce.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn; // Access token expiration in seconds
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    
    // Backward compatibility
    @Deprecated
    public String getToken() {
        return accessToken;
    }
    
    @Deprecated
    public String getType() {
        return tokenType;
    }
}