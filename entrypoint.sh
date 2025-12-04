#!/bin/bash
set -e  # Exit on any error

echo "🚀 Starting Bagisto application..."

# 1. Create necessary directories
echo "📁 Creating storage directories..."
mkdir -p /var/www/html/storage/logs
mkdir -p /var/www/html/storage/framework/sessions
mkdir -p /var/www/html/storage/framework/views
mkdir -p /var/www/html/storage/framework/cache
mkdir -p /var/www/html/storage/app/public
mkdir -p /var/www/html/bootstrap/cache

# Create log file
touch /var/www/html/storage/logs/laravel.log

# 2. Set proper permissions
echo "🔐 Setting permissions..."
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache
chmod 664 /var/www/html/storage/logs/laravel.log

# 3. Ensure .env exists
if [ ! -f /var/www/html/.env ]; then
    echo "📝 Copying .env.example to .env..."
    cp /var/www/html/.env.example /var/www/html/.env
fi

# 4. Generate APP_KEY if not set
if ! grep -q "^APP_KEY=base64:" /var/www/html/.env 2>/dev/null; then
    echo "🔑 Generating application key..."
    php artisan key:generate --force
fi

# 5. Wait for database connection
echo "🔄 Waiting for database connection..."
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if php artisan migrate:status > /dev/null 2>&1; then
        echo "✅ Database connected successfully!"
        break
    fi
    
    RETRY_COUNT=$((RETRY_COUNT + 1))
    echo "⏳ Waiting for database... (Attempt $RETRY_COUNT/$MAX_RETRIES)"
    sleep 3
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "❌ Could not connect to database after $MAX_RETRIES attempts"
    exit 1
fi

# 6. Clear all caches
echo "🧹 Clearing caches..."
php artisan config:clear
php artisan cache:clear
php artisan route:clear
php artisan view:clear
php artisan clear-compiled

# 7. Create storage link
echo "🔗 Creating storage link..."
php artisan storage:link || true  # Ignore error if link already exists

# 8. Publish vendor assets
echo "📦 Publishing vendor assets..."
php artisan vendor:publish --all --force
php artisan bagisto:publish || echo "⚠️  bagisto:publish not available, skipping..."

# 9. Run database migrations
echo "🗄️  Running database migrations..."
php artisan migrate --force

# 10. Optimize application
echo "⚡ Optimizing application..."
php artisan optimize

# 11. Final permission check
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache /var/www/html/public
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache

echo "✨ Application setup complete!"
echo "🌐 Starting Apache on port ${PORT:-8080}..."

# 12. Start Apache in foreground
exec apache2-foreground
