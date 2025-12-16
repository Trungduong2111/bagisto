# 🔧 QUICK FIX - Circular Dependency & Database Issues

## ❌ **Lỗi gặp phải:**
```
Error creating bean with name 'userService': 
Requested bean is currently in creation: Is there an unresolvable circular reference?
```

## ✅ **Đã sửa:**

### **1. Circular Dependency Fix:**
- ✅ **Tách AuthenticationConfig** ra khỏi SecurityConfig
- ✅ **Loại bỏ dependency** UserService từ SecurityConfig
- ✅ **Inject DaoAuthenticationProvider** thay vì UserService

### **2. Files đã sửa:**

#### **SecurityConfig.java:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    // ❌ Removed: private final UserService userService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    // ✅ Inject provider instead of creating it
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) {
        // ... configuration
        http.authenticationProvider(authenticationProvider);
        return http.build();
    }
}
```

#### **AuthenticationConfig.java (NEW):**
```java
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
```

### **3. Database Configuration:**
- ✅ **Tạo data-h2.sql** với H2 syntax
- ✅ **Cập nhật application.yml** để load data correctly
- ✅ **Enable defer-datasource-initialization**

### **4. Entity Builder Fix:**
- ✅ **Đổi tất cả @SuperBuilder về @Builder** (đơn giản hơn)
- ✅ **Giữ BaseEntity không có builder** để tránh conflict

## 🧪 **Test ngay:**

```bash
# Quick test
./quick-test.sh

# Hoặc manual:
cd ecommerce-backend
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🎯 **Endpoints để test:**

```bash
# Health check
curl http://localhost:8080/api/health/simple

# Swagger UI
http://localhost:8080/swagger-ui.html

# H2 Console
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:ecommerce_dev
# Username: sa
# Password: (empty)

# Login test
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

## 📋 **Expected Results:**

1. ✅ **Application starts** without circular dependency error
2. ✅ **Database tables created** automatically
3. ✅ **Sample data loaded** (4 users, 7 products, 5 categories)
4. ✅ **APIs accessible** via Swagger
5. ✅ **Authentication works** with sample accounts

## 🚨 **If still errors:**

### **Compilation errors:**
- Check Java version (need 17+)
- Verify Maven dependencies
- Clean and rebuild: `mvn clean compile`

### **Runtime errors:**
- Check application.yml syntax
- Verify H2 database configuration
- Check logs for specific error messages

### **Authentication errors:**
- Verify JWT configuration
- Check password encoding
- Test with sample accounts

## 🎉 **Success Indicators:**

```
✅ Started EcommerceBackendApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080 (http)
✅ H2 console available at '/h2-console'
✅ Swagger UI available at '/swagger-ui.html'
```

## 📞 **Quick Commands:**

```bash
# Start app
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Test health
curl http://localhost:8080/api/health/simple

# Stop app
Ctrl+C
```

Bây giờ app sẽ chạy được mà không có circular dependency! 🚀