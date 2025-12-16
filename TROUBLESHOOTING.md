# 🔧 Troubleshooting Guide

## ✅ **Đã sửa các lỗi MapStruct**

### **Vấn đề gặp phải:**
```
java: Unknown property "createdAt" in result type Product.ProductBuilder
java: Unknown property "id" in result type Order.OrderBuilder
```

### **Nguyên nhân:**
- MapStruct không thể mapping các field từ `BaseEntity` khi sử dụng `@Builder`
- Lombok `@Builder` không kế thừa fields từ class cha

### **Giải pháp đã áp dụng:**

#### **1. Cập nhật BaseEntity:**
```java
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder          // ← Thay đổi từ không có annotation
@NoArgsConstructor     // ← Thêm mới
@AllArgsConstructor    // ← Thêm mới
public abstract class BaseEntity {
    // ... fields
}
```

#### **2. Cập nhật tất cả Entities:**
```java
// Thay đổi từ @Builder thành @SuperBuilder
@Entity
@SuperBuilder  // ← Thay đổi
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    // ... fields
}
```

#### **3. Sửa Mappers:**
```java
// Chuyển từ abstract class thành interface
@Mapper(componentModel = "spring")
public interface ProductMapper {  // ← Thay đổi từ abstract class
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    // ... other mappings
    Product toEntity(ProductRequest request);
}
```

## 🧪 **Cách test build:**

### **1. Compile project:**
```bash
cd ecommerce-backend
mvn clean compile
```

### **2. Run tests:**
```bash
mvn test
```

### **3. Build JAR:**
```bash
mvn clean package
```

### **4. Run application:**
```bash
java -jar target/ecommerce-backend-0.0.1-SNAPSHOT.jar
```

## 🚨 **Nếu vẫn gặp lỗi:**

### **Lỗi MapStruct compilation:**
1. Clean project: `mvn clean`
2. Rebuild: `mvn compile`
3. Check MapStruct version trong `pom.xml`

### **Lỗi Lombok:**
1. Ensure IDE có Lombok plugin
2. Enable annotation processing
3. Rebuild project

### **Lỗi Spring Boot:**
1. Check Java version (cần Java 17+)
2. Check Spring Boot version compatibility
3. Verify all dependencies

## 📋 **Checklist sau khi sửa:**

- ✅ **BaseEntity**: Có `@SuperBuilder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- ✅ **All Entities**: Sử dụng `@SuperBuilder` thay vì `@Builder`
- ✅ **Mappers**: Chuyển thành `interface` và ignore BaseEntity fields
- ✅ **Controllers**: Sử dụng mapper methods đúng cách
- ✅ **No duplicate annotations**: Không có cả `@Builder` và `@SuperBuilder`

## 🎯 **Entities đã cập nhật:**

1. ✅ `BaseEntity` - Added `@SuperBuilder`, `@NoArgsConstructor`, `@AllArgsConstructor`
2. ✅ `User` - Changed to `@SuperBuilder`
3. ✅ `Product` - Changed to `@SuperBuilder`, removed duplicate `@Builder`
4. ✅ `Category` - Changed to `@SuperBuilder`
5. ✅ `Order` - Changed to `@SuperBuilder`
6. ✅ `OrderItem` - Changed to `@SuperBuilder`
7. ✅ `Address` - Changed to `@SuperBuilder`
8. ✅ `CartItem` - Changed to `@SuperBuilder`
9. ✅ `ProductImage` - Changed to `@SuperBuilder`
10. ✅ `ProductVariant` - Changed to `@SuperBuilder`
11. ✅ `Review` - Changed to `@SuperBuilder`
12. ✅ `RefreshToken` - Changed to `@SuperBuilder`

## 🔄 **Mappers đã cập nhật:**

1. ✅ `ProductMapper` - Changed to interface, added proper mappings
2. ✅ `OrderMapper` - Changed to interface, added custom mapping methods
3. ✅ `CategoryMapper` - Basic interface

## 🚀 **Sau khi build thành công:**

```bash
# Test API endpoints
curl http://localhost:8080/actuator/health

# Access Swagger UI
http://localhost:8080/swagger-ui.html

# Test authentication
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

## 💡 **Tips:**

1. **IDE Setup**: Ensure Lombok và MapStruct plugins được cài đặt
2. **Clean Build**: Luôn chạy `mvn clean` trước khi build
3. **Java Version**: Đảm bảo sử dụng Java 17+
4. **Dependencies**: Check tất cả dependencies trong `pom.xml`
5. **Annotation Processing**: Enable trong IDE settings