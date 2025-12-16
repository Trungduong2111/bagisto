#!/bin/bash

echo "🔍 CHECKING E-COMMERCE BACKEND READINESS"
echo "========================================"

cd ecommerce-backend

echo "📋 1. Checking project structure..."
if [ -f "pom.xml" ] && [ -f "src/main/java/com/ecommerce/backend/EcommerceBackendApplication.java" ]; then
    echo "✅ Project structure OK"
else
    echo "❌ Missing core files"
    exit 1
fi

echo ""
echo "📋 2. Checking configuration files..."
if [ -f "src/main/resources/application.yml" ] && [ -f "src/main/resources/data-h2.sql" ]; then
    echo "✅ Configuration files OK"
else
    echo "❌ Missing configuration files"
    exit 1
fi

echo ""
echo "📋 3. Checking key classes..."
REQUIRED_FILES=(
    "src/main/java/com/ecommerce/backend/config/SecurityConfig.java"
    "src/main/java/com/ecommerce/backend/config/AuthenticationConfig.java"
    "src/main/java/com/ecommerce/backend/controller/AuthController.java"
    "src/main/java/com/ecommerce/backend/controller/ProductController.java"
    "src/main/java/com/ecommerce/backend/service/UserService.java"
    "src/main/java/com/ecommerce/backend/service/RefreshTokenService.java"
    "src/main/java/com/ecommerce/backend/entity/User.java"
    "src/main/java/com/ecommerce/backend/entity/Product.java"
)

ALL_EXIST=true
for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ Missing: $file"
        ALL_EXIST=false
    fi
done

if [ "$ALL_EXIST" = false ]; then
    echo "❌ Some required files are missing"
    exit 1
fi

echo ""
echo "📋 4. Checking Maven compilation..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Maven compilation successful"
else
    echo "❌ Maven compilation failed"
    echo "Run 'mvn clean compile' to see detailed errors"
    exit 1
fi

echo ""
echo "🎉 ALL CHECKS PASSED!"
echo ""
echo "🚀 Ready to run! Execute these commands:"
echo "   cd ecommerce-backend"
echo "   mvn spring-boot:run -Dspring-boot.run.profiles=dev"
echo ""
echo "📍 Once running, access:"
echo "   • Health: http://localhost:8080/api/health/simple"
echo "   • Swagger: http://localhost:8080/swagger-ui.html"
echo "   • H2 Console: http://localhost:8080/h2-console"
echo ""
echo "👤 Test accounts:"
echo "   • admin@example.com / password123"
echo "   • john.doe@example.com / password123"