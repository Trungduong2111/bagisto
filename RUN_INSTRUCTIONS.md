# 🚀 HƯỚNG DẪN CHẠY E-COMMERCE BACKEND

## ✅ **Kiểm tra sẵn sàng:**
```bash
./check-ready.sh
```

## 🏃 **Chạy ứng dụng:**

### **Bước 1: Compile & Run**
```bash
cd ecommerce-backend
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **Bước 2: Kiểm tra app đã chạy**
Khi thấy log này là thành công:
```
✅ Started EcommerceBackendApplication in X.XXX seconds (JVM running for X.XXX)
✅ Tomcat started on port(s): 8080 (http) with context path ''
```

## 🧪 **Test ngay lập tức:**

### **1. Health Check:**
```bash
curl http://localhost:8080/api/health/simple
# Expected: "OK - E-commerce Backend is running!"
```

### **2. Swagger UI:**
Mở browser: http://localhost:8080/swagger-ui.html

### **3. H2 Database Console:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:ecommerce_dev`
- Username: `sa`
- Password: (để trống)

### **4. Test Authentication:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "abc123...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "id": 1,
  "email": "admin@example.com",
  "firstName": "Admin",
  "lastName": "User",
  "role": "SUPER_ADMIN"
}
```

### **5. Test Protected Endpoint:**
```bash
# Sử dụng accessToken từ bước 4
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

### **6. Test Product API:**
```bash
curl http://localhost:8080/api/products
```

## 📊 **Sample Data có sẵn:**

### **👥 Users:**
- `admin@example.com` / `password123` (SUPER_ADMIN)
- `manager@example.com` / `password123` (ADMIN)  
- `john.doe@example.com` / `password123` (CUSTOMER)
- `jane.smith@example.com` / `password123` (CUSTOMER)

### **📱 Products:**
- iPhone 14 Pro - $999.00
- Samsung Galaxy S23 - $899.00  
- MacBook Pro 14" - $1999.00
- Dell XPS 13 - $1299.00
- Men's T-Shirt - $29.99
- Women's Dress - $89.99
- The Great Gatsby - $12.99

### **📂 Categories:**
- Electronics (Smartphones, Laptops)
- Clothing (Men's, Women's)
- Books (Fiction)
- Home & Garden
- Sports

## 🔧 **Nếu gặp lỗi:**

### **Port đã được sử dụng:**
```bash
# Thay đổi port
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8081
```

### **Database lỗi:**
```bash
# Xóa H2 database và tạo lại
rm -rf ~/ecommerce_dev.mv.db
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### **Compilation lỗi:**
```bash
mvn clean compile
# Xem chi tiết lỗi và sửa
```

## 🎯 **Các API chính:**

### **Authentication:**
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/refresh-token` - Làm mới token
- `POST /api/auth/logout` - Đăng xuất
- `GET /api/auth/me` - Thông tin user hiện tại

### **Products:**
- `GET /api/products` - Danh sách sản phẩm
- `GET /api/products/{id}` - Chi tiết sản phẩm
- `GET /api/products/search?q=keyword` - Tìm kiếm
- `POST /api/products` - Tạo sản phẩm (Admin)

### **Orders:**
- `GET /api/orders/my-orders` - Đơn hàng của user
- `POST /api/orders` - Tạo đơn hàng
- `GET /api/orders` - Tất cả đơn hàng (Admin)

## 🎉 **Success Indicators:**

Khi app chạy thành công, bạn sẽ thấy:
- ✅ Swagger UI hoạt động
- ✅ H2 Console kết nối được
- ✅ Login API trả về JWT tokens
- ✅ Protected APIs yêu cầu authentication
- ✅ Sample data hiển thị trong database

## 📞 **Commands tóm tắt:**

```bash
# Kiểm tra
./check-ready.sh

# Chạy
cd ecommerce-backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Test
curl http://localhost:8080/api/health/simple

# Stop
Ctrl+C
```