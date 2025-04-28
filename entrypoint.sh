#!/bin/bash

cd /var/www/html


# Đợi database Railway sẵn sàng
echo "🔄 Waiting for database..."
until php artisan migrate:status > /dev/null 2>&1; do
  echo "⏳ Waiting for DB to be ready..."
  sleep 3
done
# Ensure .env exists
[ -f .env ] || cp .env.example .env

# Generate key if missing
if ! grep -q "^APP_KEY=base64:" .env; then
  echo "Generating app key..."
  php artisan key:generate
fi

# Laravel setup
php artisan config:clear
php artisan cache:clear
php artisan view:clear
php artisan storage:link
php artisan migrate --force
php artisan vendor:publish --all --force

# **Thêm lệnh migrate ở đây để đảm bảo cơ sở dữ liệu được cập nhật**
echo "🔄 Running migrations..."
php artisan migrate --force

# Publish vendor assets
php artisan vendor:publish --all --force

# Start Apache
exec apache2-foreground
