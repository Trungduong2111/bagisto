# 🐬 HƯỚNG DẪN SETUP MYSQL CHO E-COMMERCE BACKEND

## 🚀 **QUICK START:**

### **1. Cài đặt MySQL:**
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install mysql-server

# macOS
brew install mysql

# Windows: Download từ https://dev.mysql.com/downloads/mysql/
```

### **2. Start MySQL:**
```bash
# Linux
sudo systemctl start mysql
sudo systemctl enable mysql

# macOS  
brew services start mysql

# Windows: Start MySQL service
```

### **3. Setup Database:**
```bash
# Connect to MySQL
mysql -u root -p

# Tạo database
CREATE DATABASE IF NOT EXISTS ecommerce_db;

# Tạo user (optional)
CREATE USER IF NOT EXISTS 'ecommerce_user'@'localhost' IDENTIFIED BY 'ecommerce_password';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;

# Exit
EXIT;
```

### **4. Cập nhật config:**
Sửa file `ecommerce-backend/src/main/resources/application-mysql.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root  # hoặc ecommerce_user
    password: YOUR_MYSQL_PASSWORD  # ⚠️ Thay bằng password thật
```

### **5. Chạy với MySQL:**
```bash
cd ecommerce-backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## 📋 **CHI TIẾT SETUP:**

### **Option 1: Sử dụng Root User (Đơn giản)**
```sql
mysql -u root -p
CREATE DATABASE IF NOT EXISTS ecommerce_db;
EXIT;
```

Cập nhật `application-mysql.yml`:
```yaml
spring:
  datasource:
    username: root
    password: your_root_password
```

### **Option 2: Tạo User riêng (Khuyến nghị)**
```sql
mysql -u root -p

CREATE DATABASE IF NOT EXISTS ecommerce_db;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SELECT User, Host FROM mysql.user WHERE User = 'ecommerce_user';
SHOW GRANTS FOR 'ecommerce_user'@'localhost';

EXIT;
```

Cập nhật `application-mysql.yml`:
```yaml
spring:
  datasource:
    username: ecommerce_user
    password: StrongPassword123!
```

## 🔧 **TROUBLESHOOTING:**

### **Lỗi "Access denied for user":**
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

### **Lỗi Connection refused:**
```bash
# Check MySQL status
sudo systemctl status mysql

# Start MySQL
sudo systemctl start mysql

# Check port
netstat -tlnp | grep 3306
```

### **Lỗi "Public Key Retrieval":**
Thêm `allowPublicKeyRetrieval=true` vào URL:
```yaml
url: jdbc:mysql://localhost:3306/ecommerce_db?allowPublicKeyRetrieval=true&useSSL=false
```

## 🧪 **VERIFY SETUP:**

### **1. Test MySQL Connection:**
```bash
mysql -u root -p -e "SELECT VERSION();"
```

### **2. Check Database:**
```sql
mysql -u root -p
USE ecommerce_db;
SHOW TABLES;
```

### **3. Test Application:**
```bash
cd ecommerce-backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Khi thấy log này là thành công:
```
✅ HikariPool-1 - Start completed.
✅ Started EcommerceBackendApplication in X.XXX seconds
```

### **4. Verify Data:**
```sql
USE ecommerce_db;
SELECT COUNT(*) FROM users;      -- Should be 4
SELECT COUNT(*) FROM products;   -- Should be 7  
SELECT COUNT(*) FROM categories; -- Should be 10
```

## 📊 **MYSQL COMMANDS:**

### **Database Management:**
```sql
-- Show databases
SHOW DATABASES;

-- Use database
USE ecommerce_db;

-- Show tables
SHOW TABLES;

-- Describe table structure
DESCRIBE users;
DESCRIBE products;

-- Check data
SELECT * FROM users LIMIT 5;
SELECT * FROM products LIMIT 5;
```

### **Reset Database:**
```sql
DROP DATABASE IF EXISTS ecommerce_db;
CREATE DATABASE ecommerce_db;
```

### **Backup/Restore:**
```bash
# Backup
mysqldump -u root -p ecommerce_db > ecommerce_backup.sql

# Restore
mysql -u root -p ecommerce_db < ecommerce_backup.sql
```

## 🚀 **PRODUCTION SETUP:**

### **1. Create Production User:**
```sql
CREATE USER 'ecommerce_prod'@'localhost' IDENTIFIED BY 'VeryStrongPassword123!@#';
GRANT SELECT, INSERT, UPDATE, DELETE ON ecommerce_db.* TO 'ecommerce_prod'@'localhost';
FLUSH PRIVILEGES;
```

### **2. Production Config:**
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_prod?useSSL=true&serverTimezone=UTC
    username: ecommerce_prod
    password: ${MYSQL_PASSWORD}  # Environment variable
  jpa:
    hibernate:
      ddl-auto: validate  # Don't auto-create in production
    show-sql: false
```

### **3. Environment Variables:**
```bash
export MYSQL_PASSWORD=VeryStrongPassword123!@#
export JWT_ACCESS_TOKEN_SECRET=ProductionAccessTokenSecret...
export JWT_REFRESH_TOKEN_SECRET=ProductionRefreshTokenSecret...
```

## 📱 **QUICK COMMANDS:**

```bash
# Setup MySQL
./setup-mysql.sh

# Run with MySQL
./run-with-mysql.sh

# Check MySQL status
sudo systemctl status mysql

# Connect to MySQL
mysql -u root -p

# Reset database
mysql -u root -p -e "DROP DATABASE IF EXISTS ecommerce_db; CREATE DATABASE ecommerce_db;"
```

## ✅ **SUCCESS CHECKLIST:**

- ✅ MySQL service running
- ✅ Database `ecommerce_db` created  
- ✅ User has proper permissions
- ✅ Application connects successfully
- ✅ Tables created automatically
- ✅ Sample data loaded
- ✅ APIs working via Swagger

**Bây giờ bạn có thể chạy với MySQL thay vì H2!** 🎉