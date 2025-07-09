# Bagisto E-commerce Platform - Installation & Setup Guide

## 📋 Table of Contents
- [Overview](#overview)
- [System Requirements](#system-requirements)
- [Installation Methods](#installation-methods)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Development Setup](#development-setup)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## 🚀 Overview

Bagisto is a hand-tailored E-commerce framework built on some of the hottest opensource technologies such as Laravel (a PHP framework) and Vue.js (a progressive JavaScript framework).

**Key Features:**
- Multi-Channel, Multi-Currency, Multi-Locale
- Built-in REST API
- Easy theme customization
- Responsive design
- SEO friendly
- Admin dashboard
- Customer management
- Order management
- Inventory management

## 🔧 System Requirements

Before installing Bagisto, ensure your system meets the following requirements:

### Server Requirements
- **PHP**: >= 8.1
- **MySQL**: >= 5.7.8 OR **MariaDB**: >= 10.3.2
- **Composer**: >= 2.0
- **Node.js**: >= 16.x
- **NPM**: >= 8.x

### PHP Extensions
```bash
# Required PHP extensions
- OpenSSL
- PDO
- Mbstring
- Tokenizer
- XML
- Ctype
- JSON
- BCMath
- GD
- Zip
- Intl
- Exif
```

### Optional Requirements
- **Redis**: For caching and session storage
- **Elasticsearch**: For advanced search functionality
- **Supervisor**: For queue management

## 📦 Installation Methods

### Method 1: Using Composer (Recommended)

```bash
# Create new Bagisto project
composer create-project bagisto/bagisto bagisto

# Navigate to project directory
cd bagisto

# Set proper permissions
chmod -R 755 storage bootstrap/cache
```

### Method 2: Clone from GitHub

```bash
# Clone the repository
git clone https://github.com/bagisto/bagisto.git

# Navigate to project directory
cd bagisto

# Install dependencies
composer install

# Set proper permissions
chmod -R 755 storage bootstrap/cache
```

### Method 3: Using Docker

```bash
# Clone the repository
git clone https://github.com/bagisto/bagisto.git
cd bagisto

# Run with Docker Compose
docker-compose up -d

# Access the application at http://localhost
```

## 🏁 Quick Start

### 1. Environment Configuration

```bash
# Copy environment file
cp .env.example .env

# Generate application key
php artisan key:generate
```

### 2. Database Configuration

Edit `.env` file with your database credentials:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bagisto
DB_USERNAME=root
DB_PASSWORD=your_password
```

### 3. Create Database

```bash
# Create database (MySQL)
mysql -u root -p
CREATE DATABASE bagisto;
exit
```

### 4. Install Bagisto

```bash
# Run the installation command
php artisan bagisto:install

# Or run step by step
php artisan migrate
php artisan db:seed
php artisan storage:link
php artisan optimize:clear
```

### 5. Install Frontend Dependencies

```bash
# Install Node.js dependencies
npm install

# Build assets for development
npm run dev

# Or build for production
npm run build
```

### 6. Start the Application

```bash
# Start development server
php artisan serve

# Access your application at:
# Frontend: http://localhost:8000
# Admin Panel: http://localhost:8000/admin
```

## ⚙️ Configuration

### Default Admin Credentials
After installation, you can access the admin panel with:
- **Email**: `admin@example.com`
- **Password**: `admin123`

### Environment Variables

Key environment variables in `.env` file:

```env
# Application
APP_NAME=Bagisto
APP_ENV=local
APP_DEBUG=true
APP_URL=http://localhost:8000

# Database
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bagisto
DB_USERNAME=root
DB_PASSWORD=

# Mail Configuration
MAIL_DRIVER=smtp
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-password
MAIL_ENCRYPTION=tls

# Cache & Session
CACHE_DRIVER=file
SESSION_DRIVER=file
QUEUE_CONNECTION=sync

# Redis (Optional)
REDIS_HOST=127.0.0.1
REDIS_PASSWORD=null
REDIS_PORT=6379
```

### File Permissions

```bash
# Set proper permissions
sudo chown -R www-data:www-data storage bootstrap/cache
sudo chmod -R 775 storage bootstrap/cache

# Or for development
chmod -R 777 storage bootstrap/cache
```

## 🛠️ Development Setup

### 1. Enable Debug Mode

```env
APP_DEBUG=true
APP_ENV=local
```

### 2. Install Development Dependencies

```bash
# Install PHP dev dependencies
composer install --dev

# Install Node.js dev dependencies
npm install --dev
```

### 3. Run Development Tools

```bash
# Watch for file changes
npm run watch

# Run tests
php artisan test

# Clear caches
php artisan cache:clear
php artisan config:clear
php artisan route:clear
php artisan view:clear
```

### 4. Queue Management

```bash
# Start queue worker
php artisan queue:work

# For development, restart workers on code changes
php artisan queue:restart
```

### 5. Generate Sample Data

```bash
# Generate sample data for development
php artisan db:seed --class=DatabaseSeeder
```

## 🔧 Advanced Configuration

### Multi-Channel Setup

```bash
# Create additional channels via admin panel
# Or via command line
php artisan bagisto:channel:create
```

### Multi-Currency Configuration

```php
// Configure currencies in admin panel
// Admin > Settings > Currencies
```

### Search Configuration (Elasticsearch)

```env
SCOUT_DRIVER=elasticsearch
ELASTICSEARCH_HOST=localhost:9200
```

```bash
# Install Elasticsearch driver
composer require tamayo/laravel-scout-elastic

# Index products
php artisan scout:import "Webkul\Product\Models\Product"
```

## 🐛 Troubleshooting

### Common Issues

#### 1. Permission Errors
```bash
sudo chown -R www-data:www-data .
sudo chmod -R 775 storage bootstrap/cache
```

#### 2. Composer Memory Limit
```bash
php -d memory_limit=-1 /usr/local/bin/composer install
```

#### 3. Node.js Issues
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

#### 4. Database Connection Error
```bash
# Check database credentials in .env
# Ensure database exists
# Check if MySQL service is running
sudo systemctl status mysql
```

#### 5. Storage Link Issues
```bash
# Remove existing link and recreate
rm public/storage
php artisan storage:link
```

### Performance Optimization

```bash
# Cache configurations
php artisan config:cache
php artisan route:cache
php artisan view:cache

# Optimize autoloader
composer dump-autoload --optimize

# Enable OPcache in php.ini
opcache.enable=1
opcache.memory_consumption=256
opcache.max_accelerated_files=20000
```

## 🚀 Production Deployment

### 1. Environment Setup

```env
APP_ENV=production
APP_DEBUG=false
APP_URL=https://yourdomain.com
```

### 2. Optimize for Production

```bash
# Install production dependencies
composer install --optimize-autoloader --no-dev

# Build production assets
npm run build

# Cache configurations
php artisan config:cache
php artisan route:cache
php artisan view:cache

# Optimize composer autoloader
composer dump-autoload --optimize
```

### 3. Web Server Configuration

#### Apache Configuration
```apache
<VirtualHost *:80>
    DocumentRoot /path/to/bagisto/public
    ServerName yourdomain.com
    
    <Directory /path/to/bagisto/public>
        AllowOverride All
        Require all granted
    </Directory>
</VirtualHost>
```

#### Nginx Configuration
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    root /path/to/bagisto/public;
    
    index index.php;
    
    location / {
        try_files $uri $uri/ /index.php?$query_string;
    }
    
    location ~ \.php$ {
        fastcgi_pass unix:/var/run/php/php8.1-fpm.sock;
        fastcgi_index index.php;
        fastcgi_param SCRIPT_FILENAME $realpath_root$fastcgi_script_name;
        include fastcgi_params;
    }
}
```

## 📚 Additional Resources

- **Official Documentation**: https://bagisto.com/en/docs/
- **Community Forum**: https://forums.bagisto.com/
- **GitHub Repository**: https://github.com/bagisto/bagisto
- **Video Tutorials**: https://www.youtube.com/channel/UCbrWCUWBoyehUuCqbZdXY9Q

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

Bagisto is open-sourced software licensed under the [MIT license](https://opensource.org/licenses/MIT).

---

**Happy Coding! 🎉**

For more detailed information, visit the [official Bagisto documentation](https://bagisto.com/en/docs/).
