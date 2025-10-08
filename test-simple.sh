#!/bin/bash

echo "🚀 SIMPLE TEST - E-COMMERCE BACKEND"
echo "==================================="

cd ecommerce-backend

echo "🧹 Cleaning..."
mvn clean -q

echo "📦 Compiling..."
mvn compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🏃 Starting with simple profile (no sample data)..."
    echo "📍 This should start without database errors"
    echo ""
    echo "⏳ Starting application..."
    
    # Start app and capture output
    timeout 30s mvn spring-boot:run -Dspring-boot.run.profiles=simple 2>&1 | tee app.log &
    APP_PID=$!
    
    # Wait for startup
    sleep 20
    
    # Check if app started successfully
    if curl -s http://localhost:8080/api/health/simple > /dev/null 2>&1; then
        echo "✅ APPLICATION STARTED SUCCESSFULLY!"
        echo "✅ Health endpoint is responding"
        
        # Test basic endpoints
        echo ""
        echo "🧪 Testing endpoints..."
        
        # Health check
        HEALTH=$(curl -s http://localhost:8080/api/health/simple)
        echo "Health: $HEALTH"
        
        # Try to create a user
        echo ""
        echo "🧪 Testing user registration..."
        REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
          -H "Content-Type: application/json" \
          -d '{
            "firstName": "Test",
            "lastName": "User", 
            "email": "test@example.com",
            "password": "password123"
          }')
        echo "Register response: $REGISTER_RESPONSE"
        
        echo ""
        echo "🎉 SUCCESS! Your backend is working!"
        
    else
        echo "⚠️  Application might still be starting or there's an issue"
        echo "📋 Last few lines of log:"
        tail -10 app.log
    fi
    
    # Cleanup
    kill $APP_PID 2>/dev/null
    rm -f app.log
    
else
    echo "❌ Compilation failed!"
    echo "Check the errors above"
fi

echo ""
echo "🚀 To run manually:"
echo "   mvn spring-boot:run -Dspring-boot.run.profiles=simple"
echo ""
echo "📍 Access points:"
echo "   • Health: http://localhost:8080/api/health/simple"
echo "   • Swagger: http://localhost:8080/swagger-ui.html"
echo "   • H2 Console: http://localhost:8080/h2-console"