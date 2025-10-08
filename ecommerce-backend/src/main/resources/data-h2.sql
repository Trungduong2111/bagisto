-- Insert sample categories
INSERT INTO categories (name, description, slug, image_url, is_active, sort_order, created_at, updated_at) VALUES
('Electronics', 'Electronic devices and gadgets', 'electronics', '/images/categories/electronics.jpg', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Clothing', 'Fashion and apparel', 'clothing', '/images/categories/clothing.jpg', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Books', 'Books and literature', 'books', '/images/categories/books.jpg', true, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Home & Garden', 'Home improvement and gardening', 'home-garden', '/images/categories/home-garden.jpg', true, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sports', 'Sports equipment and accessories', 'sports', '/images/categories/sports.jpg', true, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert subcategories
INSERT INTO categories (name, description, slug, image_url, is_active, sort_order, parent_id, created_at, updated_at) VALUES
('Smartphones', 'Mobile phones and accessories', 'smartphones', '/images/categories/smartphones.jpg', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laptops', 'Laptops and computers', 'laptops', '/images/categories/laptops.jpg', true, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Men''s Clothing', 'Clothing for men', 'mens-clothing', '/images/categories/mens-clothing.jpg', true, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Women''s Clothing', 'Clothing for women', 'womens-clothing', '/images/categories/womens-clothing.jpg', true, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fiction', 'Fiction books', 'fiction', '/images/categories/fiction.jpg', true, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample products
INSERT INTO products (name, description, short_description, sku, price, compare_price, stock_quantity, min_stock_level, status, is_featured, slug, category_id, created_at, updated_at) VALUES
('iPhone 14 Pro', 'Latest iPhone with advanced camera system and A16 Bionic chip', 'Latest iPhone with advanced features', 'IPHONE-14-PRO', 999.00, 1099.00, 50, 5, 'ACTIVE', true, 'iphone-14-pro', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Samsung Galaxy S23', 'Flagship Android smartphone with excellent camera', 'Premium Android smartphone', 'GALAXY-S23', 899.00, 999.00, 30, 5, 'ACTIVE', true, 'samsung-galaxy-s23', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MacBook Pro 14"', 'Professional laptop with M2 chip', 'Powerful laptop for professionals', 'MACBOOK-PRO-14', 1999.00, 2199.00, 20, 3, 'ACTIVE', true, 'macbook-pro-14', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dell XPS 13', 'Ultrabook with Intel Core i7', 'Compact and powerful ultrabook', 'DELL-XPS-13', 1299.00, 1399.00, 25, 5, 'ACTIVE', false, 'dell-xps-13', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Men''s T-Shirt', 'Comfortable cotton t-shirt', 'Basic cotton t-shirt for men', 'MENS-TSHIRT-001', 29.99, 39.99, 100, 10, 'ACTIVE', false, 'mens-t-shirt', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Women''s Dress', 'Elegant evening dress', 'Beautiful dress for special occasions', 'WOMENS-DRESS-001', 89.99, 119.99, 50, 5, 'ACTIVE', true, 'womens-dress', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Great Gatsby', 'Classic American novel by F. Scott Fitzgerald', 'Timeless classic novel', 'BOOK-GATSBY', 12.99, 15.99, 200, 20, 'ACTIVE', false, 'the-great-gatsby', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample users (password is 'password123' encoded with BCrypt)
INSERT INTO users (first_name, last_name, email, password, phone, role, status, email_verified, created_at, updated_at) VALUES
('Admin', 'User', 'admin@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567890', 'SUPER_ADMIN', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('John', 'Doe', 'john.doe@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567891', 'CUSTOMER', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane', 'Smith', 'jane.smith@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567892', 'CUSTOMER', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Store', 'Manager', 'manager@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '+1234567893', 'ADMIN', 'ACTIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);