package com.ecommerce.backend.controller;

import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            response.put("found", true);
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("role", user.getRole());
            response.put("status", user.getStatus());
            response.put("emailVerified", user.getEmailVerified());
            response.put("enabled", user.isEnabled());
            response.put("accountNonLocked", user.isAccountNonLocked());
            response.put("passwordLength", user.getPassword().length());
            response.put("passwordStart", user.getPassword().substring(0, Math.min(10, user.getPassword().length())));
        } else {
            response.put("found", false);
            response.put("message", "User not found");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Map<String, Object>> verifyPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            boolean matches = passwordEncoder.matches(password, user.getPassword());
            response.put("email", email);
            response.put("passwordMatches", matches);
            response.put("userEnabled", user.isEnabled());
            response.put("accountNonLocked", user.isAccountNonLocked());
            response.put("status", user.getStatus());
        } else {
            response.put("error", "User not found");
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Map<String, Object>> createAdminUser() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Delete existing admin if exists
            userRepository.findByEmail("admin@example.com").ifPresent(userRepository::delete);
            
            // Create new admin user
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .phone("+1234567890")
                    .role(com.ecommerce.backend.enums.Role.SUPER_ADMIN)
                    .status(com.ecommerce.backend.enums.UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();
            
            User savedUser = userRepository.save(admin);
            
            response.put("success", true);
            response.put("message", "Admin user created successfully");
            response.put("userId", savedUser.getId());
            response.put("email", savedUser.getEmail());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}