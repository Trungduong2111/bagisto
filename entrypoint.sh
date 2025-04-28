#!/bin/bash

# cd /var/www/html


# # Đợi database Railway sẵn sàng
# echo "🔄 Waiting for database..."
# until php artisan migrate:status > /dev/null 2>&1; do
#   echo "⏳ Waiting for DB to be ready..."
#   sleep 3
# done
# # Ensure .env exists
# [ -f .env ] || cp .env.example .env

# # Generate key if missing
# if ! grep -q "^APP_KEY=base64:" .env; then
#   echo "Generating app key..."
#   php artisan key:generate
# fi

# # Laravel setup
# php artisan config:clear
# php artisan cache:clear
# php artisan view:clear
# php artisan storage:link
# php artisan migrate --force
# php artisan vendor:publish --all --force

# # **Thêm lệnh migrate ở đây để đảm bảo cơ sở dữ liệu được cập nhật**
# echo "🔄 Running migrations..."
# php artisan migrate --force

# # Publish vendor assets
# php artisan vendor:publish --all --force

# # Start Apache
# exec apache2-foreground

set -e

# Đặt permission đúng cho storage và cache
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache

# Nếu là lần chạy đầu tiên hoặc cần migrate DB
if [ ! -f /var/www/html/storage/installed ]; then
    # Generate app key nếu chưa có
    php artisan key:generate --force
    
    # Clear caches
    php artisan config:clear
    php artisan cache:clear
    php artisan view:clear
    
    # Tạo symbolic link storage
    php artisan storage:link
    
    # Migrate database nếu cần
    php artisan migrate --force
    
    # Publish vendor assets nếu cần
    php artisan vendor:publish --all --force
    
    # Đánh dấu đã cài đặt
    touch /var/www/html/storage/installed
fi

# Fix quyền một lần nữa để chắc chắn
find /var/www/html/storage -type d -exec chmod 775 {} \;
find /var/www/html/storage -type f -exec chmod 664 {} \;

# Đảm bảo file log có thể ghi được
touch /var/www/html/storage/logs/laravel.log
chown www-data:www-data /var/www/html/storage/logs/laravel.log
chmod 664 /var/www/html/storage/logs/laravel.log

# Optimize
php artisan optimize:clear

# Khởi động Apache
apache2-foreground
