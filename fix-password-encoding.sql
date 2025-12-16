-- Fix password encoding for sample users
-- Password: password123
-- BCrypt encoded: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.

USE ecommerce_db;

-- Update existing users with properly encoded passwords
UPDATE users SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' WHERE email = 'admin@example.com';
UPDATE users SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' WHERE email = 'manager@example.com';
UPDATE users SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' WHERE email = 'john.doe@example.com';
UPDATE users SET password = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.' WHERE email = 'jane.smith@example.com';

-- Verify the update
SELECT email, LEFT(password, 20) as password_start FROM users;

-- Also ensure users are active and email verified
UPDATE users SET status = 'ACTIVE', email_verified = true WHERE email IN ('admin@example.com', 'manager@example.com', 'john.doe@example.com', 'jane.smith@example.com');