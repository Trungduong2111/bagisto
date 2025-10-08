#!/bin/bash

echo "🔄 TESTING CIRCULAR DEPENDENCY FIX"
echo "=================================="

cd ecommerce-backend

echo "🧹 Cleaning project..."
mvn clean -q

echo "📦 Compiling..."
mvn compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🔄 Testing Spring context loading..."
    
    # Start app briefly to test context loading
    timeout 15s mvn spring-boot:run 2>&1 | tee startup.log &
    APP_PID=$!
    
    # Wait for startup
    sleep 12
    
    # Check for circular dependency errors
    if grep -q "circular" startup.log; then
        echo "❌ CIRCULAR DEPENDENCY STILL EXISTS!"
        echo "Found in logs:"
        grep -i "circular" startup.log
    elif grep -q "Started EcommerceBackendApplication" startup.log; then
        echo "✅ APPLICATION STARTED SUCCESSFULLY!"
        echo "✅ No circular dependency errors found!"
        
        # Test health endpoint
        if curl -s http://localhost:8080/api/health/simple > /dev/null 2>&1; then
            echo "✅ Health endpoint responding!"
        else
            echo "⚠️  Health endpoint not responding yet (might still be starting)"
        fi
        
    else
        echo "⚠️  Application might still be starting or other issues exist"
        echo "📋 Last few lines of startup log:"
        tail -5 startup.log
    fi
    
    # Cleanup
    kill $APP_PID 2>/dev/null
    rm -f startup.log
    
else
    echo "❌ Compilation failed!"
    echo "Fix compilation errors first"
fi

echo ""
echo "🚀 If successful, run with:"
echo "   mvn spring-boot:run"