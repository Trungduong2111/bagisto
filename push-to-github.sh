#!/bin/bash

echo "🚀 E-commerce Backend - Push to GitHub Script"
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}📋 Hướng dẫn sử dụng:${NC}"
echo "1. Tạo repository mới trên GitHub với tên: ecommerce-backend-spring-boot"
echo "2. Copy URL của repository (ví dụ: https://github.com/username/ecommerce-backend-spring-boot.git)"
echo "3. Chạy script này và paste URL khi được hỏi"
echo ""

# Get repository URL from user
echo -e "${YELLOW}🔗 Nhập URL của GitHub repository:${NC}"
read -p "Repository URL: " REPO_URL

if [ -z "$REPO_URL" ]; then
    echo -e "${RED}❌ Lỗi: Repository URL không được để trống!${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}📦 Đang chuẩn bị push code...${NC}"

# Change to ecommerce-backend directory
cd ecommerce-backend

# Set default branch to main (modern standard)
echo -e "${YELLOW}🔧 Đổi tên branch thành 'main'...${NC}"
git branch -M main

# Add remote origin
echo -e "${YELLOW}🔗 Thêm remote repository...${NC}"
git remote add origin "$REPO_URL"

# Push to GitHub
echo -e "${YELLOW}⬆️  Đang push code lên GitHub...${NC}"
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Thành công! Code đã được push lên GitHub${NC}"
    echo -e "${GREEN}🎉 Repository URL: $REPO_URL${NC}"
    echo ""
    echo -e "${BLUE}📚 Những gì đã được push:${NC}"
    echo "   • Complete Spring Boot E-commerce Backend"
    echo "   • JWT Authentication with Refresh Token"
    echo "   • RESTful APIs with OpenAPI documentation"
    echo "   • Docker containerization"
    echo "   • Database configuration (MySQL/H2)"
    echo "   • Security implementation"
    echo "   • Comprehensive README"
    echo ""
    echo -e "${BLUE}🚀 Bước tiếp theo:${NC}"
    echo "   1. Kiểm tra repository trên GitHub"
    echo "   2. Clone về máy local để development"
    echo "   3. Chạy: mvn spring-boot:run để test"
    echo "   4. Truy cập: http://localhost:8080/swagger-ui.html"
else
    echo ""
    echo -e "${RED}❌ Có lỗi xảy ra khi push code!${NC}"
    echo -e "${YELLOW}💡 Có thể do:${NC}"
    echo "   • Repository URL không đúng"
    echo "   • Chưa có quyền truy cập repository"
    echo "   • Repository đã có code (thử git pull trước)"
    echo ""
    echo -e "${YELLOW}🔧 Thử các lệnh sau để debug:${NC}"
    echo "   git remote -v"
    echo "   git status"
    echo "   git log --oneline"
fi