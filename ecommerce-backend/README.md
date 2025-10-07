# E-commerce Backend API

Một hệ thống backend hoàn chỉnh cho ứng dụng thương mại điện tử được xây dựng bằng Spring Boot.

## 🚀 Tính năng

### Quản lý người dùng
- ✅ Đăng ký và đăng nhập
- ✅ Xác thực JWT
- ✅ Quản lý hồ sơ người dùng
- ✅ Phân quyền (Customer, Admin, Super Admin)
- ✅ Quên mật khẩu và đặt lại mật khẩu
- ✅ Xác thực email

### Quản lý sản phẩm
- ✅ CRUD sản phẩm
- ✅ Phân loại sản phẩm (Categories)
- ✅ Biến thể sản phẩm (Product Variants)
- ✅ Hình ảnh sản phẩm
- ✅ Quản lý tồn kho
- ✅ Tìm kiếm và lọc sản phẩm
- ✅ Sản phẩm nổi bật

### Quản lý đơn hàng
- ✅ Tạo đơn hàng
- ✅ Theo dõi trạng thái đơn hàng
- ✅ Quản lý thanh toán
- ✅ Lịch sử đơn hàng
- ✅ Hủy đơn hàng

### Giỏ hàng
- ✅ Thêm/xóa sản phẩm
- ✅ Cập nhật số lượng
- ✅ Tính toán tổng tiền

### Đánh giá sản phẩm
- ✅ Đánh giá và nhận xét
- ✅ Xếp hạng sao
- ✅ Duyệt đánh giá

## 🛠️ Công nghệ sử dụng

- **Framework**: Spring Boot 3.1.5
- **Database**: MySQL / H2 (development)
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **Documentation**: OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Yêu cầu hệ thống

- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+ (cho production)

## 🚀 Cài đặt và chạy

### 1. Clone repository
```bash
git clone <repository-url>
cd ecommerce-backend
```

### 2. Cấu hình database

#### Sử dụng H2 (Development - mặc định)
Không cần cấu hình gì thêm, ứng dụng sẽ sử dụng H2 in-memory database.

#### Sử dụng MySQL (Production)
```bash
# Tạo database
mysql -u root -p
CREATE DATABASE ecommerce_db;
```

Cập nhật file `application.yml`:
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: your_username
    password: your_password
```

### 3. Chạy ứng dụng
```bash
# Development mode (H2 database)
mvn spring-boot:run

# Production mode (MySQL)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Truy cập ứng dụng

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (dev mode): http://localhost:8080/h2-console

## 📚 API Documentation

### Authentication Endpoints
```
POST /api/auth/register     - Đăng ký tài khoản
POST /api/auth/login        - Đăng nhập
POST /api/auth/forgot-password - Quên mật khẩu
POST /api/auth/reset-password  - Đặt lại mật khẩu
POST /api/auth/verify-email    - Xác thực email
```

### Product Endpoints
```
GET    /api/products              - Lấy danh sách sản phẩm
GET    /api/products/{id}         - Lấy sản phẩm theo ID
GET    /api/products/slug/{slug}  - Lấy sản phẩm theo slug
GET    /api/products/search       - Tìm kiếm sản phẩm
GET    /api/products/featured     - Lấy sản phẩm nổi bật
POST   /api/products              - Tạo sản phẩm mới (Admin)
PUT    /api/products/{id}         - Cập nhật sản phẩm (Admin)
DELETE /api/products/{id}         - Xóa sản phẩm (Admin)
```

### Order Endpoints
```
GET  /api/orders/my-orders        - Lấy đơn hàng của user
GET  /api/orders/{id}             - Lấy đơn hàng theo ID
POST /api/orders                  - Tạo đơn hàng mới
PATCH /api/orders/{id}/cancel     - Hủy đơn hàng
```

### Admin Endpoints
```
GET   /api/orders                 - Lấy tất cả đơn hàng (Admin)
PATCH /api/orders/{id}/status     - Cập nhật trạng thái đơn hàng (Admin)
PATCH /api/orders/{id}/payment-status - Cập nhật trạng thái thanh toán (Admin)
```

## 🔐 Authentication

API sử dụng JWT token để xác thực. Sau khi đăng nhập thành công, bạn sẽ nhận được token và cần gửi kèm trong header:

```
Authorization: Bearer <your-jwt-token>
```

## 👥 Tài khoản mặc định

Hệ thống đã tạo sẵn một số tài khoản để test:

```
Super Admin:
Email: admin@example.com
Password: password123

Admin:
Email: manager@example.com  
Password: password123

Customer:
Email: john.doe@example.com
Password: password123
```

## 🗂️ Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/ecommerce/backend/
│   │   ├── config/          # Cấu hình Spring
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA Entities
│   │   ├── enums/          # Enumerations
│   │   ├── mapper/         # MapStruct Mappers
│   │   ├── repository/     # JPA Repositories
│   │   ├── security/       # Security Configuration
│   │   └── service/        # Business Logic Services
│   └── resources/
│       ├── application.yml # Cấu hình ứng dụng
│       └── data.sql       # Dữ liệu mẫu
└── test/                  # Unit Tests
```

## 🔧 Cấu hình môi trường

### Development
```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:h2:mem:ecommerce_dev
```

### Production
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_prod
```

### Test
```yaml
spring:
  profiles:
    active: test
  datasource:
    url: jdbc:h2:mem:ecommerce_test
```

## 📊 Database Schema

### Các bảng chính:
- `users` - Thông tin người dùng
- `categories` - Danh mục sản phẩm
- `products` - Sản phẩm
- `product_variants` - Biến thể sản phẩm
- `product_images` - Hình ảnh sản phẩm
- `orders` - Đơn hàng
- `order_items` - Chi tiết đơn hàng
- `cart_items` - Giỏ hàng
- `addresses` - Địa chỉ
- `reviews` - Đánh giá sản phẩm

## 🧪 Testing

```bash
# Chạy tất cả tests
mvn test

# Chạy tests với coverage
mvn test jacoco:report
```

## 📝 Logging

Logs được lưu tại:
- Console output (development)
- `logs/ecommerce-backend.log` (production)

## 🚀 Deployment

### Docker
```bash
# Build image
docker build -t ecommerce-backend .

# Run container
docker run -p 8080:8080 ecommerce-backend
```

### JAR file
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/ecommerce-backend-0.0.1-SNAPSHOT.jar
```

## 🤝 Contributing

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📄 License

Dự án này được phân phối dưới giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.

## 📞 Liên hệ

- Email: developer@example.com
- GitHub: [Your GitHub Profile]

## 🔄 Roadmap

- [ ] Tích hợp payment gateway (VNPay, MoMo)
- [ ] Hệ thống thông báo real-time
- [ ] API cho mobile app
- [ ] Elasticsearch cho tìm kiếm nâng cao
- [ ] Redis cache
- [ ] Microservices architecture
- [ ] Docker compose cho development
- [ ] CI/CD pipeline