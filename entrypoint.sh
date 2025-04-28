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

# Vào đúng thư mục
cd /var/www/html

# Tạo thư mục cần thiết
mkdir -p storage/logs storage/framework/sessions storage/framework/views storage/framework/cache bootstrap/cache

# Tạo file log nếu chưa có
touch storage/logs/laravel.log

# Phân quyền
chown -R www-data:www-data storage bootstrap/cache public
chmod -R 775 storage bootstrap/cache
chmod 664 storage/logs/laravel.log

# Đảm bảo file .env có
if [ ! -f .env ]; then
    cp .env.example .env
fi

# Check concord.php
if [ ! -f config/concord.php ]; then
    echo "⚙️  Creating config/concord.php..."
    cp vendor/konekt/concord/config/config.php config/concord.php
fi

# Install Composer dependencies (nếu cần)
if [ ! -d vendor ]; then
    composer install --no-interaction --prefer-dist --optimize-autoloader
fi

# Chờ database sẵn sàng
echo "🔄 Waiting for database..."
until php artisan migrate:status > /dev/null 2>&1; do
  echo "⏳ Waiting for DB to be ready..."
  sleep 3
done

# Clear caches
php artisan config:clear
php artisan cache:clear
php artisan route:clear
php artisan view:clear
php artisan clear-compiled

# Optimize app
php artisan optimize

# Storage symlink
php artisan storage:link

# Generate app key nếu chưa có
if ! grep -q "^APP_KEY=base64:" .env; then
    echo "🔑 Generating app key..."
    php artisan key:generate --force
fi

# Publish vendor assets
php artisan vendor:publish --all --force
php artisan bagisto:publish

# Update URL trong app.php nếu cần (nên để tự động theo biến môi trường, nhưng bạn đang hardcode thì sửa luôn)
sed -i "s|http://bagisto-production-e2fd.up.railway.app|https://bagisto-production-e2fd.up.railway.app|g" config/app.php

# Migrate database
php artisan migrate --force

# Final permissions fix
chown -R www-data:www-data /var/www/html

# Start Apache
echo "🚀 Starting Apache..."
exec apache2-foreground
