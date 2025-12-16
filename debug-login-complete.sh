#!/bin/bash

echo "🔍 COMPLETE LOGIN DEBUG"
echo "======================="

echo "📋 1. Check if application is running..."
HEALTH=$(curl -s http://localhost:8080/api/health/simple 2>/dev/null)

if [[ "$HEALTH" == *"running"* ]]; then
    echo "✅ Application is running"
else
    echo "❌ Application not running!"
    echo "Start with: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "📋 2. Check if users exist in database..."
USERS_RESPONSE=$(curl -s http://localhost:8080/api/test/users 2>/dev/null)

if [[ "$USERS_RESPONSE" == *"admin@example.com"* ]]; then
    echo "✅ Admin user exists in database"
else
    echo "⚠️  Admin user not found in database"
    echo "Creating admin user..."
    
    CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/test/create-admin 2>/dev/null)
    echo "Create admin response: $CREATE_RESPONSE"
    
    if [[ "$CREATE_RESPONSE" == *"success\":true"* ]]; then
        echo "✅ Admin user created successfully"
    else
        echo "❌ Failed to create admin user"
        exit 1
    fi
fi

echo ""
echo "📋 3. Check user details..."
USER_DETAILS=$(curl -s http://localhost:8080/api/test/user/admin@example.com 2>/dev/null)
echo "Admin user details: $USER_DETAILS"

if [[ "$USER_DETAILS" == *"\"found\":true"* ]]; then
    echo "✅ Admin user found with correct details"
else
    echo "❌ Admin user details incorrect"
fi

echo ""
echo "📋 4. Test password verification..."
PASSWORD_TEST=$(curl -s -X POST http://localhost:8080/api/test/verify-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}' 2>/dev/null)

echo "Password verification: $PASSWORD_TEST"

if [[ "$PASSWORD_TEST" == *"\"passwordMatches\":true"* ]]; then
    echo "✅ Password verification successful"
else
    echo "❌ Password verification failed"
fi

echo ""
echo "📋 5. Test actual login..."
LOGIN_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}' 2>/dev/null)

echo "Login response: $LOGIN_RESPONSE"
HTTP_CODE=$(echo "$LOGIN_RESPONSE" | grep "HTTP_CODE:" | cut -d: -f2)

if [[ "$HTTP_CODE" == "200" ]]; then
    echo "✅ LOGIN SUCCESSFUL!"
    
    # Extract token for further testing
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
    if [ ! -z "$TOKEN" ]; then
        echo "✅ Access token received"
        
        # Test protected endpoint
        echo ""
        echo "📋 6. Testing protected endpoint..."
        ME_RESPONSE=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/auth/me 2>/dev/null)
        echo "Protected endpoint response: $ME_RESPONSE"
        
        if [[ "$ME_RESPONSE" == *"admin@example.com"* ]]; then
            echo "✅ Protected endpoint working!"
        else
            echo "⚠️  Protected endpoint issue"
        fi
    fi
    
elif [[ "$HTTP_CODE" == "401" ]]; then
    echo "❌ LOGIN FAILED - 401 Unauthorized"
    echo "Check password encoding or user status"
elif [[ "$HTTP_CODE" == "400" ]]; then
    echo "❌ LOGIN FAILED - 400 Bad Request"
    echo "Check JSON format or validation"
else
    echo "❌ LOGIN FAILED - HTTP Code: $HTTP_CODE"
fi

echo ""
echo "🎯 SUMMARY:"
echo "If login still fails, check:"
echo "1. MySQL database connection"
echo "2. Users table has data"
echo "3. Password encoding is correct"
echo "4. User status is ACTIVE and email_verified is true"
echo ""
echo "🔧 Manual fixes:"
echo "# Create admin user manually"
echo "mysql -u root -p < create-admin-user.sql"
echo ""
echo "# Check users in database"
echo "mysql -u root -p -e 'USE ecommerce_db; SELECT email, role, status, email_verified FROM users;'"