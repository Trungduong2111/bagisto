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

# Đảm bảo thư mục logs và các thư mục khác tồn tại
mkdir -p /var/www/html/storage/logs
mkdir -p /var/www/html/storage/framework/sessions
mkdir -p /var/www/html/storage/framework/views
mkdir -p /var/www/html/storage/framework/cache
mkdir -p /var/www/html/bootstrap/cache

# Tạo file log nếu chưa tồn tại
touch /var/www/html/storage/logs/laravel.log

# Cấp quyền đúng cho cả thư mục và file
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache
chmod 664 /var/www/html/storage/logs/laravel.log

# Kiểm tra file concord.php
if [ ! -f /var/www/html/config/concord.php ]; then
    cp /var/www/html/vendor/konekt/concord/config/config.php /var/www/html/config/concord.php
fi

# Clear cache để đảm bảo cấu hình mới được áp dụng
php artisan config:clear
php artisan cache:clear
php artisan route:clear
php artisan view:clear

# Tạo symlink cho storage
php artisan storage:link

# Tạo app key nếu chưa có
php artisan key:generate --force

# Khởi động Apache
exec apache2-foreground
