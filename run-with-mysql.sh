#!/bin/bash

echo "🐬 RUNNING E-COMMERCE BACKEND WITH MYSQL"
echo "========================================"

cd ecommerce-backend

echo "📋 1. Checking MySQL connection..."
mysql -u root -p -e "SELECT 1;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ MySQL connection OK"
else
    echo "❌ MySQL connection failed!"
    echo "Please check:"
    echo "  1. MySQL service is running"
    echo "  2. Username/password is correct"
    echo "  3. Database 'ecommerce_db' exists"
    echo ""
    echo "Run: ./setup-mysql.sh for help"
    exit 1
fi

echo ""
echo "📋 2. Compiling project..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful"
echo ""

echo "📋 3. Starting application with MySQL..."
echo "🔗 MySQL Database: ecommerce_db"
echo "🌐 Application will be available at: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo ""
echo "⏹️  Press Ctrl+C to stop"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=mysql