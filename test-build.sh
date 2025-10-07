#!/bin/bash

echo "🔧 Testing Maven Build..."
echo "========================="

cd ecommerce-backend

echo "📦 Running Maven compile..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compile successful!"
    echo ""
    echo "🧪 Running tests..."
    mvn test
    
    if [ $? -eq 0 ]; then
        echo "✅ Tests passed!"
        echo ""
        echo "📦 Building JAR..."
        mvn package -DskipTests
        
        if [ $? -eq 0 ]; then
            echo "✅ Build successful!"
            echo "🎉 JAR file created: target/ecommerce-backend-0.0.1-SNAPSHOT.jar"
        else
            echo "❌ Build failed!"
        fi
    else
        echo "⚠️ Some tests failed, but continuing with build..."
        mvn package -DskipTests
    fi
else
    echo "❌ Compile failed! Check the errors above."
fi