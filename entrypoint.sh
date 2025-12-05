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

# 3. Tạo file .env CƠ SỞ nếu chưa có (chỉ để tránh lỗi)
if [ ! -f /var/www/html/.env ]; then
    echo "📝 Tạo file .env cơ sở..."
    cp /var/www/html/.env.example /var/www/html/.env
fi

# 4. XỬ LÝ APP_KEY - ƯU TIÊN BIẾN MÔI TRƯỜNG CAO NHẤT
echo "🔐 Xử lý APP_KEY..."
if [ -n "${APP_KEY}" ]; then
    # TRƯỜNG HỢP 1: Đã set biến môi trường APP_KEY trong Dashboard -> DÙNG CÁI NÀY
    echo "   ✅ Lấy APP_KEY từ biến môi trường Render."
    # Cập nhật vào file .env vật lý để đảm bảo artisan commands hoạt động
    sed -i "s|^APP_KEY=.*|APP_KEY=${APP_KEY}|" /var/www/html/.env
elif grep -q '^APP_KEY=base64:' /var/www/html/.env 2>/dev/null; then
    # TRƯỜNG HỢP 2: File .env đã có key hợp lệ -> Giữ nguyên
    echo "   ℹ️  APP_KEY đã tồn tại trong file .env."
else
    # TRƯỜNG HỢP 3: Không có nguồn nào -> TẠO MỚI (chỉ dành cho local/dev)
    echo "   ⚠️  Cảnh báo: Tạo APP_KEY mới. Trên production, hãy set biến APP_KEY trong Render Dashboard."
    php artisan key:generate --force
       # Đọc key vừa tạo
    export APP_KEY=$(grep '^APP_KEY=' /var/www/html/.env | cut -d'=' -f2)
    echo ""
    echo "   ╔════════════════════════════════════════════════════════════╗"
    echo "   ║  ⚠️  QUAN TRỌNG: LƯU APP_KEY NÀY NGAY!                    ║"
    echo "   ╚════════════════════════════════════════════════════════════╝"
    echo ""
    echo "   📋 Copy key này và lưu vào Render Environment Variables:"
    echo ""
    echo "      APP_KEY=${APP_KEY}"
    echo ""
    echo "   🔐 Sau khi lưu vào Render, deploy lại để không bị tạo key mới."
    echo ""
fi

# 5. KIỂM TRA KẾT NỐI DATABASE VỚI BIẾN MÔI TRƯỜNG
echo "🔄 Kiểm tra kết nối database..."
MAX_RETRIES=30
RETRY_COUNT=0
while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    # Đoạn script PHP này sẽ đọc BIẾN MÔI TRƯỜNG HỆ THỐNG trực tiếp
    if php -r "
    // getenv() lấy trực tiếp từ biến môi trường hệ thống
    \$host = getenv('DB_HOST');
    \$port = getenv('DB_PORT');
    \$dbname = getenv('DB_DATABASE');
    \$user = getenv('DB_USERNAME');
    \$pass = getenv('DB_PASSWORD');
    
    if (empty(\$host) || empty(\$user) || empty(\$pass)) {
        echo 'ERROR: Thiếu biến môi trường kết nối database.';
        exit(1);
    }
    
    try {
        \$dsn = 'pgsql:host=' . \$host . ';port=' . (\$port ?: '5432') . ';dbname=' . (\$dbname ?: 'postgres');
        new PDO(\$dsn, \$user, \$pass, [PDO::ATTR_TIMEOUT => 5, PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]);
        exit(0);
    } catch (PDOException \$e) {
        echo 'ERROR: ' . \$e->getMessage();
        exit(1);
    }
    " 2>/dev/null; then
        echo "✅ Kết nối database thành công!"
        break
    fi
    RETRY_COUNT=$((RETRY_COUNT + 1))
    echo "⏳ Đang thử lại... (Lần thứ $RETRY_COUNT/$MAX_RETRIES)"
    sleep 3
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "❌ Không thể kết nối database sau $MAX_RETRIES lần thử."
    echo "   Hãy kiểm tra các biến DB_HOST, DB_USERNAME, DB_PASSWORD trong Render Dashboard."
    exit 1
fi

# ========== THỨ TỰ ĐÚNG: MIGRATE → CLEAR CACHE ==========

# 6. CHẠY MIGRATION TRƯỚC TIÊN (tạo tất cả bảng bao gồm bảng cache)
# echo "🗄️  Đang chạy database migrations..."
# php artisan migrate --force

# # 7. SAU KHI CÓ ĐỦ BẢNG → XÓA CACHE
# echo "🧹 Xóa cache..."
# php artisan config:clear
# php artisan cache:clear
# php artisan route:clear
# php artisan view:clear

# # 8. PUBLISH ASSETS (nếu cần)
# echo "📦 Publishing assets..."
# php artisan vendor:publish --all --force 2>/dev/null || true

# 9. Tối ưu hóa và hoàn tất
echo "⚡ Tối ưu hóa ứng dụng..."
php artisan optimize

echo "🔗 Tạo storage link..."
php artisan storage:link --force 2>/dev/null || true

# 10. Final permission check
chown -R www-data:www-data /var/www/html/storage /var/www/html/bootstrap/cache /var/www/html/public
chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache

echo "✨ Application setup complete!"
echo "🌐 Starting Apache on port ${PORT:-8080}..."

# 11. Start Apache in foreground
exec apache2-foreground
