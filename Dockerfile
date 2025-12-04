# Base PHP + Apache image
FROM php:8.3-apache

# ARGs & ENV
ARG APP_ENV=production
ARG APP_URL=https://shop-production-e1a9.up.railway.app
ENV APP_ENV=${APP_ENV}
ENV APP_URL=${APP_URL}
ENV PORT=8080
ENV DEBIAN_FRONTEND=noninteractive

# Update package lists and install dependencies
# Support both MySQL and PostgreSQL
RUN apt-get update && apt-get install -y \
    git unzip zip ffmpeg \
    libzip-dev zlib1g-dev \
    libfreetype-dev libjpeg62-turbo-dev libpng-dev libwebp-dev libxpm-dev \
    libicu-dev libgmp-dev libmagickwand-dev imagemagick \
    gcc make autoconf pkg-config \
    libonig-dev curl \
    default-mysql-client \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# PHP extensions (support both MySQL and PostgreSQL)
RUN docker-php-ext-configure gd --with-freetype --with-jpeg --with-webp && \
    docker-php-ext-install -j$(nproc) gd && \
    docker-php-ext-configure intl && \
    docker-php-ext-install -j$(nproc) intl && \
    docker-php-ext-install -j$(nproc) bcmath calendar exif gmp mysqli pdo pdo_mysql pdo_pgsql pgsql zip

# Install imagick
RUN pecl install imagick && docker-php-ext-enable imagick

# Composer
COPY --from=composer:2.7 /usr/bin/composer /usr/local/bin/composer

# Node.js
COPY --from=node:20 /usr/local/bin/node /usr/local/bin/node
COPY --from=node:20 /usr/local/lib/node_modules /usr/local/lib/node_modules
RUN ln -s /usr/local/lib/node_modules/npm/bin/npm-cli.js /usr/local/bin/npm && \
    npm install -g npx

# Apache config
COPY ./.config/apache.conf /etc/apache2/sites-available/000-default.conf
RUN a2enmod rewrite && \
    echo "ServerName localhost" >> /etc/apache2/apache2.conf

# Update Apache to listen on dynamic PORT
RUN sed -i "s/Listen 80/Listen ${PORT}/" /etc/apache2/ports.conf && \
    sed -i "s/:80>/:${PORT}>/" /etc/apache2/sites-available/000-default.conf

# Set working directory
WORKDIR /var/www/html

# Copy source code
COPY . /var/www/html/

# Create necessary directories
RUN mkdir -p /var/www/html/storage/logs \
    /var/www/html/storage/framework/sessions \
    /var/www/html/storage/framework/views \
    /var/www/html/storage/framework/cache \
    /var/www/html/bootstrap/cache

# Install composer dependencies
RUN composer install --no-interaction --prefer-dist --optimize-autoloader --no-dev

# Prepare environment file
RUN if [ ! -f .env ]; then cp .env.example .env; fi

# Set proper permissions
RUN chown -R www-data:www-data /var/www/html && \
    chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache && \
    find /var/www/html -type f -exec chmod 644 {} \; && \
    find /var/www/html -type d -exec chmod 755 {} \; && \
    chmod -R 775 /var/www/html/storage /var/www/html/bootstrap/cache

# Copy and prepare entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE ${PORT}

ENTRYPOINT ["/entrypoint.sh"]
