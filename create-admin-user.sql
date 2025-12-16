-- Create admin user if not exists
-- This script can be run manually if login fails

USE ecommerce_db;

-- Delete existing admin if exists (to avoid duplicates)
DELETE FROM users WHERE email = 'admin@example.com';

-- Insert admin user with properly encoded password
-- Password: password123
-- BCrypt: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.
INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) 
VALUES ('Admin', 'User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567890', 'SUPER_ADMIN', 'ACTIVE', true, NOW(), NOW());

-- Verify the user was created
SELECT id, first_name, last_name, email, role, status, email_verified FROM users WHERE email = 'admin@example.com';

-- Also create a test customer
DELETE FROM users WHERE email = 'test@example.com';
INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) 
VALUES ('Test', 'User', 'test@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567894', 'CUSTOMER', 'ACTIVE', true, NOW(), NOW());

-- Show all users
SELECT id, first_name, last_name, email, role, status, email_verified FROM users;