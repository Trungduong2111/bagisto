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

# Create necessary folders
mkdir -p /var/www/html/storage/logs
mkdir -p /var/www/html/storage/framework/sessions
mkdir -p /var/www/html/storage/framework/views
mkdir -p /var/www/html/storage/framework/cache
mkdir -p /var/www/html/bootstrap/cache

# Touch log file
touch /var/www/html/storage/logs/laravel.log

# Set permissions
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache /var/www/html/public
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache
chmod -R 775 /var/www/html/storage/logs
chmod 664 /var/www/html/storage/logs/laravel.log

# Ensure .env exists
if [ ! -f /var/www/html/.env ]; then
    cp /var/www/html/.env.example /var/www/html/.env
fi

# Composer install
composer install --no-interaction --prefer-dist --optimize-autoloader

# Check database ready
echo "🔄 Waiting for database..."
until php artisan migrate:status > /dev/null 2>&1; do
  echo "⏳ Waiting for DB to be ready..."
  sleep 3
done

# Check config file concord.php
if [ ! -f /var/www/html/config/concord.php ]; then
    cp /var/www/html/vendor/konekt/concord/config/config.php /var/www/html/config/concord.php
fi

# Fix APP_URL if needed
sed -i "s|http://bagisto-production-e2fd.up.railway.app|https://bagisto-production-e2fd.up.railway.app|g" /var/www/html/config/app.php

# Laravel optimize
php artisan config:clear
php artisan cache:clear
php artisan route:clear
php artisan view:clear
php artisan clear-compiled
php artisan optimize

# Storage link
php artisan storage:link

# Generate app key
php artisan key:generate --force

# Publish vendor
php artisan vendor:publish --all --force
php artisan bagisto:publish

# Migrate database
php artisan migrate --force

# Start apache
exec apache2-foreground
