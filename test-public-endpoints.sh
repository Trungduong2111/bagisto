#!/bin/bash

echo "🔓 TESTING PUBLIC ENDPOINTS"
echo "=========================="

echo "📋 1. Health check (should work)..."
curl -s http://localhost:8080/api/health/simple
echo ""

echo "📋 2. Register endpoint (should work)..."
REGISTER_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "password123"
  }')

echo "Register response: $REGISTER_RESPONSE"
REG_CODE=$(echo "$REGISTER_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)

if [[ "$REG_CODE" == "200" ]]; then
    echo "✅ Registration endpoint working!"
else
    echo "❌ Registration failed with code: $REG_CODE"
fi

echo ""
echo "📋 3. Create admin via test endpoint..."
curl -s -X POST http://localhost:8080/api/test/create-admin

echo ""
echo "📋 4. Login endpoint (should work now)..."
LOGIN_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}')

echo "Login response: $LOGIN_RESPONSE"
LOGIN_CODE=$(echo "$LOGIN_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)

if [[ "$LOGIN_CODE" == "200" ]]; then
    echo "✅ LOGIN SUCCESSFUL!"
    
    # Extract token
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
    if [ ! -z "$TOKEN" ]; then
        echo "✅ Access token received: ${TOKEN:0:20}..."
        
        # Test protected endpoint
        echo ""
        echo "📋 5. Testing protected endpoint..."
        ME_RESPONSE=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me)
        echo "Protected endpoint response: $ME_RESPONSE"
    fi
    
elif [[ "$LOGIN_CODE" == "401" ]]; then
    echo "❌ LOGIN STILL FAILING - 401"
    echo "Check application logs for authentication errors"
else
    echo "❌ LOGIN FAILED - Code: $LOGIN_CODE"
fi

echo ""
echo "🎯 SUMMARY:"
if [[ "$REG_CODE" == "200" ]] && [[ "$LOGIN_CODE" == "200" ]]; then
    echo "✅ ALL ENDPOINTS WORKING!"
    echo "✅ JWT Filter fixed - public endpoints accessible"
    echo "✅ Authentication working properly"
else
    echo "⚠️  Some endpoints still have issues"
    echo "Check application logs and database"
fi