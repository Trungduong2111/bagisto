# 🔍 LOGIN TROUBLESHOOTING GUIDE

## ❌ **Your Issue:**
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
→ 401 Unauthorized
```

## 🔧 **STEP-BY-STEP DEBUG:**

### **Step 1: Check if app is running**
```bash
curl http://localhost:8080/api/health/simple
# Expected: "OK - E-commerce Backend is running!"
```

### **Step 2: Run complete debug**
```bash
./quick-debug.sh
```

This will:
- ✅ Check if users exist in database
- ✅ Create admin user if missing
- ✅ Verify password encoding
- ✅ Test actual login

### **Step 3: Manual database check**
```sql
mysql -u root -p
USE ecommerce_db;

-- Check if users table exists and has data
SHOW TABLES;
SELECT email, role, status, email_verified FROM users;

-- If no users, run this:
source create-admin-user.sql;
```

### **Step 4: Create admin manually via API**
```bash
curl -X POST http://localhost:8080/api/test/create-admin
```

### **Step 5: Test login again**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

## 🎯 **COMMON CAUSES & FIXES:**

### **1. User doesn't exist in database**
**Fix:**
```bash
curl -X POST http://localhost:8080/api/test/create-admin
```

### **2. Password encoding mismatch**
**Check:**
```bash
curl -X POST http://localhost:8080/api/test/verify-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
```

**Fix:** Run `create-admin-user.sql`

### **3. User status not ACTIVE**
**Check:**
```bash
curl http://localhost:8080/api/test/user/admin@example.com
```

**Fix:**
```sql
UPDATE users SET status = 'ACTIVE', email_verified = true WHERE email = 'admin@example.com';
```

### **4. Database not loaded with sample data**
**Fix:**
```sql
mysql -u root -p ecommerce_db < ecommerce-backend/src/main/resources/data.sql
```

### **5. Spring Security configuration issue**
**Check logs for:**
- Authentication provider errors
- UserDetailsService errors
- Password encoder errors

## 🧪 **QUICK TESTS:**

### **Test 1: Health Check**
```bash
curl http://localhost:8080/api/health/simple
# Expected: "OK - E-commerce Backend is running!"
```

### **Test 2: Check Users**
```bash
curl http://localhost:8080/api/test/users
# Should show list of users
```

### **Test 3: Create Admin**
```bash
curl -X POST http://localhost:8080/api/test/create-admin
# Should create admin user
```

### **Test 4: Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password123"}'
# Should return JWT tokens
```

## 📊 **Expected Success Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "abc123def456...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "id": 1,
  "email": "admin@example.com",
  "firstName": "Admin",
  "lastName": "User",
  "role": "SUPER_ADMIN"
}
```

## 🚨 **If Still Failing:**

### **Check Application Logs:**
```bash
tail -f ecommerce-backend/logs/ecommerce-backend.log
# Look for authentication errors
```

### **Restart with Clean Database:**
```bash
# Stop application (Ctrl+C)
# Drop and recreate database
mysql -u root -p -e "DROP DATABASE IF EXISTS ecommerce_db; CREATE DATABASE ecommerce_db;"
# Start application again
mvn spring-boot:run
# Run debug script
./quick-debug.sh
```

### **Manual User Creation:**
```sql
mysql -u root -p
USE ecommerce_db;

DELETE FROM users WHERE email = 'admin@example.com';

INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) 
VALUES ('Admin', 'User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567890', 'SUPER_ADMIN', 'ACTIVE', true, NOW(), NOW());

SELECT email, role, status, email_verified FROM users WHERE email = 'admin@example.com';
```

**Run `./quick-debug.sh` to find the exact issue!** 🔍