# 🐬 SETUP CHỈ DÙNG MYSQL

## ✅ **Đã loại bỏ H2, chỉ dùng MySQL:**

### **📋 Những gì đã thay đổi:**
- ❌ **Xóa H2 dependency** khỏi `pom.xml`
- ❌ **Xóa H2 profiles** (dev, test)
- ✅ **Chỉ giữ MySQL** làm database chính
- ✅ **Simplified configuration** chỉ có 2 profiles: default + prod

## 🚀 **SETUP NHANH:**

### **Bước 1: Setup MySQL**
```bash
./setup-mysql-only.sh
```

### **Bước 2: Tạo database**
```sql
mysql -u root -p
CREATE DATABASE IF NOT EXISTS ecommerce_db;
EXIT;
```

### **Bước 3: Cập nhật password**
Sửa file `ecommerce-backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    username: root
    password: YOUR_MYSQL_PASSWORD  # ⚠️ THAY BẰNG PASSWORD THẬT
```

### **Bước 4: Chạy ứng dụng**
```bash
./run-mysql.sh
```

## 📋 **CONFIGURATION:**

### **Default Profile (Development):**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: password  # ⚠️ THAY BẰNG PASSWORD CỦA BẠN
    
  jpa:
    hibernate:
      ddl-auto: create    # Tạo tables tự động
    show-sql: true        # Hiển thị SQL queries
    
  sql:
    init:
      mode: always              # Load sample data
      data-locations: classpath:data.sql
```

### **Production Profile:**
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: ${DATABASE_URL:jdbc:mysql://localhost:3306/ecommerce_prod}
    username: ${DATABASE_USERNAME:ecommerce_user}
    password: ${DATABASE_PASSWORD:secure_password}
    
  jpa:
    hibernate:
      ddl-auto: validate  # Không tự động thay đổi schema
    show-sql: false       # Không hiển thị SQL
    
  sql:
    init:
      mode: never         # Không load sample data
```

## 🧪 **TEST:**

### **1. Chạy với default profile:**
```bash
mvn spring-boot:run
```

### **2. Chạy với production profile:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### **3. Test endpoints:**
```bash
# Health check
curl http://localhost:8080/api/health/simple

# Login với sample account
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

## 📊 **SAMPLE DATA:**

Khi chạy lần đầu, hệ thống sẽ tự động tạo:
- **4 users**: admin, manager, john.doe, jane.smith
- **7 products**: iPhone, Samsung, MacBook, Dell, T-Shirt, Dress, Book
- **10 categories**: Electronics, Clothing, Books, etc.

### **Test Accounts:**
- **Admin**: admin@example.com / password123
- **Manager**: manager@example.com / password123  
- **Customer**: john.doe@example.com / password123

## 🔧 **TROUBLESHOOTING:**

### **Lỗi "Access denied":**
```bash
# Reset MySQL root password
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'new_password';
FLUSH PRIVILEGES;
EXIT;
```

### **Lỗi "Unknown database":**
```sql
mysql -u root -p
CREATE DATABASE ecommerce_db;
EXIT;
```

### **Lỗi "Connection refused":**
```bash
# Start MySQL service
sudo systemctl start mysql
# hoặc
brew services start mysql
```

## 📱 **ACCESS POINTS:**

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/health/simple
- **Actuator**: http://localhost:8080/actuator/health

## 🎯 **QUICK COMMANDS:**

```bash
# Setup MySQL
./setup-mysql-only.sh

# Run application  
./run-mysql.sh

# Check MySQL
mysql -u root -p -e "USE ecommerce_db; SHOW TABLES;"

# View sample data
mysql -u root -p -e "USE ecommerce_db; SELECT * FROM users; SELECT * FROM products LIMIT 5;"
```

## ✅ **SUCCESS CHECKLIST:**

- ✅ MySQL service running
- ✅ Database `ecommerce_db` exists
- ✅ Application connects successfully  
- ✅ Tables created automatically
- ✅ Sample data loaded
- ✅ APIs working via Swagger
- ✅ Authentication working with sample accounts

**Bây giờ chỉ dùng MySQL, không còn H2!** 🎉