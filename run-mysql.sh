#!/bin/bash

echo "🐬 CHẠY E-COMMERCE BACKEND VỚI MYSQL"
echo "==================================="

cd ecommerce-backend

echo "📋 1. Kiểm tra MySQL connection..."
mysql -u root -p -e "USE ecommerce_db; SELECT 1;" 2>/dev/null

if [ $? -ne 0 ]; then
    echo "❌ Không thể kết nối MySQL!"
    echo ""
    echo "Kiểm tra:"
    echo "1. MySQL service đang chạy: sudo systemctl status mysql"
    echo "2. Database tồn tại: mysql -u root -p -e 'SHOW DATABASES;'"
    echo "3. Password đúng trong application.yml"
    echo ""
    echo "Chạy setup: ./setup-mysql-only.sh"
    exit 1
fi

echo "✅ MySQL connection OK"
echo ""

echo "📋 2. Compiling project..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful"
echo ""

echo "📋 3. Starting application..."
echo "🔗 Database: mysql://localhost:3306/ecommerce_db"
echo "🌐 Application: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo ""
echo "⏹️  Press Ctrl+C to stop"
echo ""

mvn spring-boot:run