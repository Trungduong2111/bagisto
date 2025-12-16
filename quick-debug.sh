#!/bin/bash

echo "🔍 QUICK LOGIN DEBUG"
echo "==================="

echo "📋 1. Testing if users exist..."
curl -s http://localhost:8080/api/test/users | head -20

echo ""
echo "📋 2. Creating admin user manually..."
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/test/create-admin)
echo "Create admin response: $CREATE_RESPONSE"

echo ""
echo "📋 3. Checking admin user details..."
curl -s http://localhost:8080/api/test/user/admin@example.com

echo ""
echo "📋 4. Testing password verification..."
curl -s -X POST http://localhost:8080/api/test/verify-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'

echo ""
echo "📋 5. Testing login again..."
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'

echo ""