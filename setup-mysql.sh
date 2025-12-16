#!/bin/bash

echo "🐬 MYSQL SETUP FOR E-COMMERCE BACKEND"
echo "====================================="

echo "📋 Step 1: Install MySQL (if not installed)"
echo "For Ubuntu/Debian:"
echo "  sudo apt update"
echo "  sudo apt install mysql-server"
echo ""
echo "For Windows:"
echo "  Download from: https://dev.mysql.com/downloads/mysql/"
echo ""
echo "For macOS:"
echo "  brew install mysql"
echo ""

echo "📋 Step 2: Start MySQL service"
echo "Linux: sudo systemctl start mysql"
echo "macOS: brew services start mysql"
echo "Windows: Start MySQL service from Services"
echo ""

echo "📋 Step 3: Create database and user"
echo "Run these MySQL commands:"
echo ""
cat << 'EOF'
-- Connect to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE IF NOT EXISTS ecommerce_db;

-- Create user (optional, can use root)
CREATE USER IF NOT EXISTS 'ecommerce_user'@'localhost' IDENTIFIED BY 'ecommerce_password';

-- Grant permissions
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SHOW DATABASES;
USE ecommerce_db;
SHOW TABLES;

-- Exit
EXIT;
EOF

echo ""
echo "📋 Step 4: Update application configuration"
echo "Edit ecommerce-backend/src/main/resources/application-mysql.yml:"
echo "  - Update MySQL password"
echo "  - Update username if using custom user"
echo ""

echo "📋 Step 5: Run application with MySQL profile"
echo "cd ecommerce-backend"
echo "mvn spring-boot:run -Dspring-boot.run.profiles=mysql"
echo ""

echo "🔧 Quick MySQL Commands:"
echo "# Connect to MySQL"
echo "mysql -u root -p"
echo ""
echo "# Check database"
echo "USE ecommerce_db;"
echo "SHOW TABLES;"
echo "SELECT * FROM users;"
echo ""
echo "# Reset database (if needed)"
echo "DROP DATABASE ecommerce_db;"
echo "CREATE DATABASE ecommerce_db;"