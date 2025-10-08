#!/bin/bash

echo "🚀 Quick Test - E-commerce Backend"
echo "=================================="

cd ecommerce-backend

echo "🧹 Cleaning project..."
mvn clean -q

echo "📦 Compiling (with detailed errors)..."
mvn compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🏃 Starting application (will stop after 30 seconds)..."
    echo "📍 Access points:"
    echo "   • Health: http://localhost:8080/api/health/simple"
    echo "   • Swagger: http://localhost:8080/swagger-ui.html"
    echo "   • H2 Console: http://localhost:8080/h2-console"
    echo ""
    
    # Start app in background and kill after 30 seconds
    timeout 30s mvn spring-boot:run -Dspring-boot.run.profiles=dev || echo "⏰ App stopped after 30 seconds"
    
else
    echo "❌ Compilation failed!"
    echo ""
    echo "🔍 Common issues to check:"
    echo "   • Circular dependency in SecurityConfig"
    echo "   • Missing dependencies in pom.xml"
    echo "   • MapStruct annotation processing"
    echo "   • Lombok configuration"
fi