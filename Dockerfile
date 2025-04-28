# Base PHP + Apache image
FROM php:8.3-apache

# ARGs & ENV
ARG APP_ENV=production
ARG APP_URL=https://shop-production-e1a9.up.railway.app
ENV APP_ENV=${APP_ENV}
ENV APP_URL=${APP_URL}
ENV PORT=8080

# Mirrors nhanh hơn (optional)
RUN echo "deb http://ftp.jp.debian.org/debian bookworm main" > /etc/apt/sources.list && \
    echo "deb http://security.debian.org/debian-security bookworm-security main" >> /etc/apt/sources.list && \
    echo "deb http://ftp.jp.debian.org/debian bookworm-updates main" >> /etc/apt/sources.list

# Cài các dependency cần thiết
RUN apt-get update && apt-get install -y \
    git unzip zip ffmpeg \
    libzip-dev zlib1g-dev \
    libfreetype6-dev libjpeg62-turbo-dev libpng-dev libwebp-dev libxpm-dev \
    libicu-dev libgmp-dev libmagickwand-dev imagemagick \
    gcc make autoconf pkg-config \
    libonig-dev curl
    
RUN apt-get update && apt-get install -y default-mysql-client

# PHP extensions
RUN docker-php-ext-configure gd --with-freetype --with-jpeg --with-webp && docker-php-ext-install gd
RUN docker-php-ext-configure intl && docker-php-ext-install intl
RUN docker-php-ext-install bcmath calendar exif gmp mysqli pdo pdo_mysql zip
RUN pecl install imagick && docker-php-ext-enable imagick

# Composer
COPY --from=composer:2.7 /usr/bin/composer /usr/local/bin/composer

# Node.js
COPY --from=node:20 /usr/local/bin/node /usr/local/bin/node
COPY --from=node:20 /usr/local/lib/node_modules /usr/local/lib/node_modules
RUN ln -s /usr/local/lib/node_modules/npm/bin/npm-cli.js /usr/local/bin/npm
RUN npm install -g npx

# Apache config
COPY ./.config/apache.conf /etc/apache2/sites-available/000-default.conf
RUN a2enmod rewrite
RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf

# Update Apache to listen on dynamic PORT
RUN sed -i "s/Listen 80/Listen ${PORT}/" /etc/apache2/ports.conf && \
    sed -i "s/:80>/:${PORT}>/" /etc/apache2/sites-available/000-default.conf
    
# Copy source code
COPY . /var/www/html/

# Phân quyền
RUN chown -R www-data:www-data /var/www/html && chmod -R 775 /var/www/html

# Laravel setup
WORKDIR /var/www/html

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

RUN composer install --no-interaction --prefer-dist --optimize-autoloader

RUN if [ ! -f .env ]; then cp .env.example .env; fi

# RUN php artisan key:generate \
#     && php artisan config:clear \
#     && php artisan cache:clear \
#     && php artisan view:clear \
#     && php artisan storage:link \
#     && php artisan migrate --force \
#     && php artisan vendor:publish --all --force

# Build frontend (dù không dùng Vue vẫn build CSS/JS của theme)
# RUN npm install && npm run build

# Entrypoint
ENTRYPOINT ["/entrypoint.sh"]

EXPOSE ${PORT}
