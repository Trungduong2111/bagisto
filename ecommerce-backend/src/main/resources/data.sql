-- Insert sample categories
INSERT INTO categories (name, description, slug, image_url, is_active, sort_order, created_at, updated_at) VALUES
('Electronics', 'Electronic devices and gadgets', 'electronics', '/images/categories/electronics.jpg', true, 1, NOW(), NOW()),
('Clothing', 'Fashion and apparel', 'clothing', '/images/categories/clothing.jpg', true, 2, NOW(), NOW()),
('Books', 'Books and literature', 'books', '/images/categories/books.jpg', true, 3, NOW(), NOW()),
('Home & Garden', 'Home improvement and gardening', 'home-garden', '/images/categories/home-garden.jpg', true, 4, NOW(), NOW()),
('Sports', 'Sports equipment and accessories', 'sports', '/images/categories/sports.jpg', true, 5, NOW(), NOW());

-- Insert subcategories
INSERT INTO categories (name, description, slug, image_url, is_active, sort_order, parent_id, created_at, updated_at) VALUES
('Smartphones', 'Mobile phones and accessories', 'smartphones', '/images/categories/smartphones.jpg', true, 1, 1, NOW(), NOW()),
('Laptops', 'Laptops and computers', 'laptops', '/images/categories/laptops.jpg', true, 2, 1, NOW(), NOW()),
('Men''s Clothing', 'Clothing for men', 'mens-clothing', '/images/categories/mens-clothing.jpg', true, 1, 2, NOW(), NOW()),
('Women''s Clothing', 'Clothing for women', 'womens-clothing', '/images/categories/womens-clothing.jpg', true, 2, 2, NOW(), NOW()),
('Fiction', 'Fiction books', 'fiction', '/images/categories/fiction.jpg', true, 1, 3, NOW(), NOW());

-- Insert sample users (password is 'password123' encoded with BCrypt)
INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) VALUES
('Admin', 'User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567890', 'SUPER_ADMIN', 'ACTIVE', true, NOW(), NOW()),
('John', 'Doe', 'john.doe@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567891', 'CUSTOMER', 'ACTIVE', true, NOW(), NOW()),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567892', 'CUSTOMER', 'ACTIVE', true, NOW(), NOW()),
('Store', 'Manager', 'manager@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567893', 'ADMIN', 'ACTIVE', true, NOW(), NOW());

-- Insert sample products
INSERT INTO products (name, description, short_description, sku, price, compare_price, stock_quantity, min_stock_level, status, is_featured, slug, category_id, created_at, updated_at) VALUES
('iPhone 14 Pro', 'Latest iPhone with advanced camera system and A16 Bionic chip', 'Latest iPhone with advanced features', 'IPHONE-14-PRO', 999.00, 1099.00, 50, 5, 'ACTIVE', true, 'iphone-14-pro', 6, NOW(), NOW()),
('Samsung Galaxy S23', 'Flagship Android smartphone with excellent camera', 'Premium Android smartphone', 'GALAXY-S23', 899.00, 999.00, 30, 5, 'ACTIVE', true, 'samsung-galaxy-s23', 6, NOW(), NOW()),
('MacBook Pro 14"', 'Professional laptop with M2 chip', 'Powerful laptop for professionals', 'MACBOOK-PRO-14', 1999.00, 2199.00, 20, 3, 'ACTIVE', true, 'macbook-pro-14', 7, NOW(), NOW()),
('Dell XPS 13', 'Ultrabook with Intel Core i7', 'Compact and powerful ultrabook', 'DELL-XPS-13', 1299.00, 1399.00, 25, 5, 'ACTIVE', false, 'dell-xps-13', 7, NOW(), NOW()),
('Men''s T-Shirt', 'Comfortable cotton t-shirt', 'Basic cotton t-shirt for men', 'MENS-TSHIRT-001', 29.99, 39.99, 100, 10, 'ACTIVE', false, 'mens-t-shirt', 8, NOW(), NOW()),
('Women''s Dress', 'Elegant evening dress', 'Beautiful dress for special occasions', 'WOMENS-DRESS-001', 89.99, 119.99, 50, 5, 'ACTIVE', true, 'womens-dress', 9, NOW(), NOW()),
('The Great Gatsby', 'Classic American novel by F. Scott Fitzgerald', 'Timeless classic novel', 'BOOK-GATSBY', 12.99, 15.99, 200, 20, 'ACTIVE', false, 'the-great-gatsby', 10, NOW(), NOW());

-- Insert sample users (password is 'password123' encoded with BCrypt)
INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) VALUES
('Admin', 'User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567890', 'SUPER_ADMIN', 'ACTIVE', true, NOW(), NOW()),
('John', 'Doe', 'john.doe@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567891', 'CUSTOMER', 'ACTIVE', true, NOW(), NOW()),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567892', 'CUSTOMER', 'ACTIVE', true, NOW(), NOW()),
('Store', 'Manager', 'manager@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567893', 'ADMIN', 'ACTIVE', true, NOW(), NOW());

-- Insert sample addresses
INSERT INTO addresses (user_id, type, first_name, last_name, address_line_1, address_line_2, city, state, postal_code, country, phone, is_default, created_at, updated_at) VALUES
(2, 'BOTH', 'John', 'Doe', '123 Main Street', 'Apt 4B', 'New York', 'NY', '10001', 'United States', '+1234567891', true, NOW(), NOW()),
(3, 'BOTH', 'Jane', 'Smith', '456 Oak Avenue', '', 'Los Angeles', 'CA', '90210', 'United States', '+1234567892', true, NOW(), NOW());

-- Insert sample product images
INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order, created_at, updated_at) VALUES
(1, '/images/products/iphone-14-pro-1.jpg', 'iPhone 14 Pro front view', true, 1, NOW(), NOW()),
(1, '/images/products/iphone-14-pro-2.jpg', 'iPhone 14 Pro back view', false, 2, NOW(), NOW()),
(2, '/images/products/galaxy-s23-1.jpg', 'Samsung Galaxy S23 front view', true, 1, NOW(), NOW()),
(3, '/images/products/macbook-pro-14-1.jpg', 'MacBook Pro 14 inch', true, 1, NOW(), NOW()),
(4, '/images/products/dell-xps-13-1.jpg', 'Dell XPS 13', true, 1, NOW(), NOW()),
(5, '/images/products/mens-tshirt-1.jpg', 'Men''s T-Shirt', true, 1, NOW(), NOW()),
(6, '/images/products/womens-dress-1.jpg', 'Women''s Dress', true, 1, NOW(), NOW()),
(7, '/images/products/gatsby-book-1.jpg', 'The Great Gatsby book cover', true, 1, NOW(), NOW());

-- Insert sample product variants
INSERT INTO product_variants (product_id, name, sku, price, stock_quantity, is_active, attribute_1_name, attribute_1_value, attribute_2_name, attribute_2_value, created_at, updated_at) VALUES
(1, 'iPhone 14 Pro 128GB Space Black', 'IPHONE-14-PRO-128-BLACK', 999.00, 20, true, 'Storage', '128GB', 'Color', 'Space Black', NOW(), NOW()),
(1, 'iPhone 14 Pro 256GB Space Black', 'IPHONE-14-PRO-256-BLACK', 1099.00, 15, true, 'Storage', '256GB', 'Color', 'Space Black', NOW(), NOW()),
(1, 'iPhone 14 Pro 128GB Gold', 'IPHONE-14-PRO-128-GOLD', 999.00, 15, true, 'Storage', '128GB', 'Color', 'Gold', NOW(), NOW()),
(5, 'Men''s T-Shirt Small Blue', 'MENS-TSHIRT-S-BLUE', 29.99, 25, true, 'Size', 'S', 'Color', 'Blue', NOW(), NOW()),
(5, 'Men''s T-Shirt Medium Blue', 'MENS-TSHIRT-M-BLUE', 29.99, 30, true, 'Size', 'M', 'Color', 'Blue', NOW(), NOW()),
(5, 'Men''s T-Shirt Large Blue', 'MENS-TSHIRT-L-BLUE', 29.99, 25, true, 'Size', 'L', 'Color', 'Blue', NOW(), NOW()),
(5, 'Men''s T-Shirt Small Red', 'MENS-TSHIRT-S-RED', 29.99, 20, true, 'Size', 'S', 'Color', 'Red', NOW(), NOW());

-- Insert sample orders
INSERT INTO orders (order_number, user_id, status, subtotal, tax_amount, shipping_amount, total_amount, currency, payment_method, payment_status, 
                   shipping_first_name, shipping_last_name, shipping_address_line_1, shipping_city, shipping_postal_code, shipping_country, shipping_phone,
                   billing_first_name, billing_last_name, billing_address_line_1, billing_city, billing_postal_code, billing_country, billing_phone,
                   created_at, updated_at) VALUES
('ORD202401010001', 2, 'DELIVERED', 999.00, 79.92, 0.00, 1078.92, 'USD', 'CREDIT_CARD', 'PAID',
 'John', 'Doe', '123 Main Street', 'New York', '10001', 'United States', '+1234567891',
 'John', 'Doe', '123 Main Street', 'New York', '10001', 'United States', '+1234567891',
 NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
('ORD202401010002', 3, 'SHIPPED', 89.99, 7.20, 9.99, 107.18, 'USD', 'PAYPAL', 'PAID',
 'Jane', 'Smith', '456 Oak Avenue', 'Los Angeles', '90210', 'United States', '+1234567892',
 'Jane', 'Smith', '456 Oak Avenue', 'Los Angeles', '90210', 'United States', '+1234567892',
 NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY);

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, product_variant_id, quantity, unit_price, total_price, product_name, product_sku, product_image_url, variant_name, created_at, updated_at) VALUES
(1, 1, 1, 1, 999.00, 999.00, 'iPhone 14 Pro', 'IPHONE-14-PRO', '/images/products/iphone-14-pro-1.jpg', 'iPhone 14 Pro 128GB Space Black', NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 7 DAY),
(2, 6, NULL, 1, 89.99, 89.99, 'Women''s Dress', 'WOMENS-DRESS-001', '/images/products/womens-dress-1.jpg', NULL, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY);

-- Insert sample cart items
INSERT INTO cart_items (user_id, product_id, product_variant_id, quantity, created_at, updated_at) VALUES
(2, 3, NULL, 1, NOW(), NOW()),
(2, 5, 4, 2, NOW(), NOW()),
(3, 2, NULL, 1, NOW(), NOW());

-- Insert sample reviews
INSERT INTO reviews (user_id, product_id, rating, title, comment, is_verified_purchase, is_approved, helpful_count, created_at, updated_at) VALUES
(2, 1, 5, 'Excellent phone!', 'The iPhone 14 Pro is amazing. Great camera quality and performance.', true, true, 5, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),
(3, 6, 4, 'Beautiful dress', 'Love the design and quality. Fits perfectly!', true, true, 3, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(2, 7, 5, 'Classic literature', 'One of the best books I''ve ever read. Highly recommended!', true, true, 8, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 10 DAY);