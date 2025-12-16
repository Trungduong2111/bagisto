#!/bin/bash

echo "🔍 DEBUG LOGIN ISSUE"
echo "===================="

echo "📋 1. Testing application health..."
HEALTH_RESPONSE=$(curl -s http://localhost:8080/api/health/simple 2>/dev/null)

if [[ "$HEALTH_RESPONSE" == *"running"* ]]; then
    echo "✅ Application is running"
else
    echo "❌ Application not responding. Is it started?"
    echo "Start with: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "📋 2. Testing login endpoint..."
LOGIN_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}' 2>/dev/null)

echo "Response: $LOGIN_RESPONSE"
echo ""

# Extract HTTP code
HTTP_CODE=$(echo "$LOGIN_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)

if [[ "$HTTP_CODE" == "200" ]]; then
    echo "✅ Login successful!"
elif [[ "$HTTP_CODE" == "401" ]]; then
    echo "❌ Login failed - 401 Unauthorized"
    echo "Possible issues:"
    echo "  • Wrong email/password"
    echo "  • User not found in database"
    echo "  • Password encoding mismatch"
elif [[ "$HTTP_CODE" == "400" ]]; then
    echo "❌ Login failed - 400 Bad Request"
    echo "Possible issues:"
    echo "  • Invalid JSON format"
    echo "  • Missing required fields"
elif [[ "$HTTP_CODE" == "500" ]]; then
    echo "❌ Login failed - 500 Internal Server Error"
    echo "Check application logs for details"
else
    echo "❌ Unexpected response code: $HTTP_CODE"
fi

echo ""
echo "📋 3. Testing user registration (create test user)..."
REG_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "password123"
  }' 2>/dev/null)

echo "Registration response: $REG_RESPONSE"
REG_HTTP_CODE=$(echo "$REG_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)

if [[ "$REG_HTTP_CODE" == "200" ]]; then
    echo "✅ Registration successful!"
    echo ""
    echo "📋 4. Testing login with new user..."
    NEW_LOGIN_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"test@example.com","password":"password123"}' 2>/dev/null)
    
    echo "New user login: $NEW_LOGIN_RESPONSE"
    NEW_HTTP_CODE=$(echo "$NEW_LOGIN_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)
    
    if [[ "$NEW_HTTP_CODE" == "200" ]]; then
        echo "✅ New user login successful!"
    else
        echo "❌ New user login failed with code: $NEW_HTTP_CODE"
    fi
else
    echo "⚠️  Registration failed with code: $REG_HTTP_CODE"
fi

echo ""
echo "🔧 TROUBLESHOOTING STEPS:"
echo "1. Check if MySQL is running and database exists"
echo "2. Check application logs for errors"
echo "3. Verify sample data was loaded"
echo "4. Check password encoding"
echo ""
echo "📋 Manual checks:"
echo "# Check if app is running"
echo "curl http://localhost:8080/api/health/simple"
echo ""
echo "# Check database (if using MySQL)"
echo "mysql -u root -p -e 'USE ecommerce_db; SELECT email, password FROM users LIMIT 5;'"