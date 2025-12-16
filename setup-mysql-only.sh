#!/bin/bash

echo "🐬 SETUP MYSQL CHO E-COMMERCE BACKEND"
echo "===================================="

echo "📋 Bước 1: Kiểm tra MySQL"
if command -v mysql &> /dev/null; then
    echo "✅ MySQL đã được cài đặt"
else
    echo "❌ MySQL chưa được cài đặt!"
    echo ""
    echo "Cài đặt MySQL:"
    echo "Ubuntu/Debian: sudo apt install mysql-server"
    echo "macOS: brew install mysql"
    echo "Windows: Tải từ https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

echo ""
echo "📋 Bước 2: Kiểm tra MySQL service"
if systemctl is-active --quiet mysql 2>/dev/null || brew services list | grep mysql | grep started &>/dev/null; then
    echo "✅ MySQL service đang chạy"
else
    echo "⚠️  MySQL service chưa chạy"
    echo "Start MySQL:"
    echo "Linux: sudo systemctl start mysql"
    echo "macOS: brew services start mysql"
fi

echo ""
echo "📋 Bước 3: Tạo database"
echo "Chạy các lệnh MySQL sau:"
echo ""

cat << 'EOF'
-- Kết nối MySQL
mysql -u root -p

-- Tạo database
CREATE DATABASE IF NOT EXISTS ecommerce_db;

-- Kiểm tra
SHOW DATABASES;
USE ecommerce_db;

-- Thoát
EXIT;
EOF

echo ""
echo "📋 Bước 4: Cập nhật password trong application.yml"
echo "Sửa file: ecommerce-backend/src/main/resources/application.yml"
echo "Dòng 16: password: YOUR_MYSQL_PASSWORD"
echo ""

echo "📋 Bước 5: Test kết nối"
read -p "Nhập MySQL password để test: " -s MYSQL_PASS
echo ""

mysql -u root -p$MYSQL_PASS -e "SELECT 1;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Kết nối MySQL thành công!"
    echo ""
    echo "🚀 Sẵn sàng chạy ứng dụng:"
    echo "   cd ecommerce-backend"
    echo "   mvn spring-boot:run"
else
    echo "❌ Không thể kết nối MySQL!"
    echo "Kiểm tra lại username/password"
fi